package org.ISKor.service;

import org.ISKor.dto.*;
import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;
import org.ISKor.exceptions.OwnerServiceException;
import org.ISKor.repositories.OwnerRepository;
import org.ISKor.transformator.KittyTransformator;
import org.ISKor.transformator.OwnerTransformator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class OwnerServiceImpl {
    private final OwnerRepository ownerRepository;
    private final KafkaTemplate<String, OwnerDTO> getOwner;
    private final KafkaTemplate<String, OwnerListDTO> getOwners;
    private final KafkaTemplate<String, KittyListDTO> getKitties;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository, KafkaTemplate<String, OwnerDTO> getOwner, KafkaTemplate<String, OwnerListDTO> getOwners, KafkaTemplate<String, KittyListDTO> getKitties) {
        this.ownerRepository = ownerRepository;
        this.getOwner = getOwner;
        this.getOwners = getOwners;
        this.getKitties = getKitties;
    }

    @Transactional
    @KafkaListener(topics = "create_owner", groupId = "groupIdCO", containerFactory = "createOwnerFactory")
    public OwnerDTO createOwner(StartOwnerDTO ownerCreateDto) {
        var owner = new Owner(ownerCreateDto.name(), ownerCreateDto.birthDate(), new ArrayList<>());
        ownerRepository.save(owner);
        var transformator = new OwnerTransformator();
        return transformator.castDTO(owner);
    }

    @Transactional
    @KafkaListener(topics = "get_by_id_owner", groupId = "groupIdGBIDO", containerFactory = "byIdOwnerFactory")
    public void getOwnerById(int id) {
        if (!ownerRepository.existsById(id)){
            throw OwnerServiceException.noOwner(id);
        }
        var owner = ownerRepository.getReferenceById(id);
        var transformator = new OwnerTransformator();
        getOwner.send("got_by_id_owner", transformator.castDTO(owner));
    }

    @Transactional
    @KafkaListener(topics = "get_by_id_owners_kitties", groupId = "groupIdGBIDOK", containerFactory = "byIdOwnerFactory")
    public void findAllKitties(int id) {
        if (!ownerRepository.existsById(id)){
            throw OwnerServiceException.noOwner(id);
        }
        var owner = ownerRepository.getReferenceById(id);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new KittyTransformator();
        for (Cat tmp : owner.getCats()){
            cats.add(transformator.castDTO(tmp));
        }
        getKitties.send("got_by_id_owners_kitties", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "get_owners", groupId = "groupIdGO", containerFactory = "byIdOwnerFactory")
    public void findAllOwners(int trash) {
        var owners = new ArrayList<OwnerDTO>();
        var transformator = new OwnerTransformator();
        for (Owner owner : ownerRepository.findAll()){
            owners.add(transformator.castDTO(owner));
        }
        getOwners.send("got_owners", new OwnerListDTO(owners));
    }

    @Transactional
    @KafkaListener(topics = "remove_owner", groupId = "groupIdRO", containerFactory = "byIdOwnerFactory")
    public void removeOwner(int id) {
        if (!ownerRepository.existsById(id)){
            throw OwnerServiceException.noOwner(id);
        }
        ownerRepository.deleteById(id);
    }
}
