package org.ISKor.services;

import org.ISKor.dto.*;
import org.ISKor.entity.Cat;
import org.ISKor.exceptions.KittyServiceException;
import org.ISKor.models.Breed;
import org.ISKor.models.Colour;
import org.ISKor.repositories.CatRepository;
import org.ISKor.repositories.OwnerRepository;
import org.ISKor.repositories.UserRepository;
import org.ISKor.transformator.KittyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class KittyServiceImpl implements KittyService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, KittyDTO> getKitty;
    private final KafkaTemplate<String, KittyListDTO> getKitties;

    @Autowired
    public KittyServiceImpl(
            CatRepository kittyRepository,
            OwnerRepository ownerRepository,
            UserRepository userRepository,
            KafkaTemplate<String, KittyDTO> getKitty,
            KafkaTemplate<String, KittyListDTO> getKitties) {
        this.catRepository = kittyRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
        this.getKitty = getKitty;
        this.getKitties = getKitties;
    }

    @Transactional
    @KafkaListener(topics = "create_kitty", groupId = "groupIdCK", containerFactory = "createKittyFactory")
    public KittyDTO createKitty(StartKittyDTO catDTO) {
        if (!ownerRepository.existsById(catDTO.ownerId())) {
            throw KittyServiceException.noOwner(catDTO.ownerId());
        }
        var tmp_colour = Colour.None;
        for (int i = 0; i < Colour.values().length; i++) {
            if (Colour.values()[i].name().equals(catDTO.colour())) {
                tmp_colour = Colour.valueOf(catDTO.colour());
            }
        }
        var tmp_breed = Breed.None;
        for (int i = 0; i < Breed.values().length; i++) {
            if (Breed.values()[i].name().equals(catDTO.breed())) {
                tmp_breed = Breed.valueOf(catDTO.breed());
            }
        }
        var owner = ownerRepository.getReferenceById(catDTO.ownerId());
        var cat = new Cat(catDTO.name(), owner, tmp_breed, tmp_colour, catDTO.birthDate(), new ArrayList<>());
        owner.addCat(cat);
        catRepository.save(cat);
        var mapper = new KittyMapper();
        return mapper.castDTO(cat);
    }

    @Transactional
    @KafkaListener(topics = "befriend", groupId = "groupIdBefriend", containerFactory = "friendFactory")
    public void makeFriends(KittyFriendsDTO friendsDto) {
        if (!catRepository.existsById(friendsDto.friend1())) {
            throw KittyServiceException.Kitty(friendsDto.friend1());
        }
        var firstCat = catRepository.getReferenceById(friendsDto.friend1());
        if (!catRepository.existsById(friendsDto.friend2())) {
            throw KittyServiceException.Kitty(friendsDto.friend2());
        }
        var secondCat = catRepository.getReferenceById(friendsDto.friend2());
        firstCat.makeFriend(secondCat);
        secondCat.makeFriend(firstCat);
        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    @Transactional
    @KafkaListener(topics = "unfriend", groupId = "groupIdUnfriend", containerFactory = "friendFactory")
    public void unfriendKitties(KittyFriendsDTO friendsDto) {
        if (!catRepository.existsById(friendsDto.friend1())) {
            throw KittyServiceException.Kitty(friendsDto.friend1());
        }
        var firstCat = catRepository.getReferenceById(friendsDto.friend1());
        if (!catRepository.existsById(friendsDto.friend2())) {
            throw KittyServiceException.Kitty(friendsDto.friend2());
        }
        var secondCat = catRepository.getReferenceById(friendsDto.friend2());
        firstCat.unfriend(secondCat);
        secondCat.unfriend(firstCat);
        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    @Transactional
    @KafkaListener(topics = "get_by_id_kitty", groupId = "groupIdGBIDK", containerFactory = "byIdKittyFactory")
    public void getKittyById(int id) {
        if (!catRepository.existsById(id)) {
            throw KittyServiceException.Kitty(id);
        }
        var cat = catRepository.getReferenceById(id);
        var mapper = new KittyMapper();
        getKitty.send("got_by_id_kitty", mapper.castDTO(cat));
    }

    @Transactional
    @KafkaListener(topics = "get_kitties_admin", groupId = "groupIdGBIDKs", containerFactory = "byIdKittyFactory")
    public void findAllKittiesForAdmin(int trash) {
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : catRepository.findAll()) {
            cats.add(mapper.castDTO(tmp));
        }
        getKitties.send("got_kitties", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "get_kitties_by_owner", groupId = "groupIdGBIDKs", containerFactory = "byIdKittyFactory")
    public void findAllKittiesByOwner(int ownerId) {
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : catRepository.findAll()) {
            if (tmp.getOwner().getId() == ownerId) {
                cats.add(mapper.castDTO(tmp));
            }
        }
        getKitties.send("got_kitties", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "get_by_id_friends", groupId = "groupIdGBIDKF", containerFactory = "byIdKittyFactory")
    public void findAllFriends(int id) {
        if (!catRepository.existsById(id)) {
            throw KittyServiceException.Kitty(id);
        }
        var cat = catRepository.getReferenceById(id);
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : cat.getFriends()){
            cats.add(mapper.castDTO(tmp));
        }
        getKitties.send("got_by_id_friends", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "remove_kitty", groupId = "groupIdRK", containerFactory = "byIdKittyFactory")
    public void removeKitty(int id) {
        if (!catRepository.existsById(id)) {
            throw KittyServiceException.Kitty(id);
        }
        var cat = catRepository.getReferenceById(id);
        for (Cat tmp : cat.getFriends()){
            tmp.getFriends().remove(cat);
        }
        cat.getOwner().getCats().remove(cat);
        cat.getFriends().clear();

        catRepository.save(cat);
        catRepository.deleteById(id);
    }

    @Transactional
    @KafkaListener(topics = "get_kitties_by_breed", groupId = "groupByBreed", containerFactory = "filterFactory")
    public void findKittiesByBreed(FilterDTO filterDto) {
        Breed breed = Breed.valueOf(filterDto.breed());
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getBreed().equals(breed)){
                cats.add(mapper.castDTO(tmp));
            }
        }
        getKitties.send("got_kitties_by_filters", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "get_kitties_by_colour", groupId = "groupByColour", containerFactory = "filterFactory")
    public void findKittiesByColour(FilterDTO filterDto) {
        Colour colour = Colour.valueOf(filterDto.colour());
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour)){
                cats.add(mapper.castDTO(tmp));
            }
        }
        getKitties.send("got_kitties_by_filters", new KittyListDTO(cats));
    }

    @Transactional
    @KafkaListener(topics = "get_kitties_by_filters", groupId = "groupByColourAndBreed", containerFactory = "filterFactory")
    public void findKittiesByColourAndBreed(FilterDTO filterDto) {
        Colour colour = Colour.valueOf(filterDto.colour());
        Breed breed = Breed.valueOf(filterDto.breed());
        var cats = new ArrayList<KittyDTO>();
        var mapper = new KittyMapper();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour) && tmp.getBreed().equals(breed)){
                cats.add(mapper.castDTO(tmp));
            }
        }
        getKitties.send("got_kitties_by_filters", new KittyListDTO(cats));
    }
}
