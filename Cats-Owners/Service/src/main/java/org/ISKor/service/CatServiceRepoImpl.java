package org.ISKor.service;

import org.ISKor.dto.KittyDTO;
import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;
import org.ISKor.entity.User;
import org.ISKor.exception.CatServiceException;
import org.ISKor.models.Breed;
import org.ISKor.models.Colour;
import org.ISKor.repositories.CatRepository;
import org.ISKor.repositories.OwnerRepository;
import org.ISKor.repositories.UserRepository;
import org.ISKor.transformators.CatTransformator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatServiceRepoImpl implements CatService{
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CatServiceRepoImpl(CatRepository catRepository, OwnerRepository ownerRepository, UserRepository userRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public KittyDTO createCat(String name, int ownerId, String breed, String colour, LocalDate birth) throws CatServiceException {
        if (!ownerRepository.existsById(ownerId)) {
            throw CatServiceException.noOwner(ownerId);
        }
        var tmp_colour = Colour.None;
        for (int i = 0; i < Colour.values().length; i++) {
            if (Colour.values()[i].name().equals(colour)) {
                tmp_colour = Colour.valueOf(colour);
            }
        }
        var tmp_breed = Breed.None;
        for (int i = 0; i < Breed.values().length; i++) {
            if (Breed.values()[i].name().equals(breed)) {
                tmp_breed = Breed.valueOf(breed);
            }
        }
        var owner = ownerRepository.getReferenceById(ownerId);
        var cat = new Cat(name, owner, tmp_breed, tmp_colour, birth, new ArrayList<>());
        owner.addCat(cat);
        catRepository.save(cat);
        var transformator = new CatTransformator();
        return transformator.castDTO(cat);
    }

    @Override
    public void deleteCat(int id) throws CatServiceException {
        if (!catRepository.existsById(id)) {
            throw CatServiceException.noCat(id);
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

    @Override
    public KittyDTO getCatById(int id) {
        if (!catRepository.existsById(id)) {
            throw CatServiceException.noCat(id);
        }
        var cat = catRepository.getReferenceById(id);
        var transformator = new CatTransformator();
        return transformator.castDTO(cat);
    }

    @Override
    public List<KittyDTO> getAllCats() {
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            cats.add(transformator.castDTO(tmp));
        }
        return cats;
    }

    public List<KittyDTO> getAllOwnersCats() {
        Owner owner = getCurrentOwner();
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getOwner().getId() == owner.getId()){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    @Override
    public List<KittyDTO> getAllFriends(int id) {
        if (!catRepository.existsById(id)) {
            throw CatServiceException.noCat(id);
        }
        var cat = catRepository.getReferenceById(id);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : cat.getFriends()){
            cats.add(transformator.castDTO(tmp));
        }
        return cats;
    }

    @Override
    public List<KittyDTO> getCatsByBreed(String breedName) {
        Breed breed = Breed.valueOf(breedName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getBreed().equals(breed)){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    public List<KittyDTO> getOwnersCatsByBreed(String breedName) {
        Owner owner = getCurrentOwner();
        Breed breed = Breed.valueOf(breedName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getBreed().equals(breed) && tmp.getOwner().getId() == owner.getId()){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    @Override
    public List<KittyDTO> getCatsByColour(String colourName) {
        Colour colour = Colour.valueOf(colourName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour)){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    public List<KittyDTO> getOwnersCatsByColour(String colourName) {
        Owner owner = getCurrentOwner();
        Colour colour = Colour.valueOf(colourName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour) && tmp.getOwner().getId() == owner.getId()){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    @Override
    public List<KittyDTO> getCatsByColourAndBreed(String colourName, String breedName) {
        Colour colour = Colour.valueOf(colourName);
        Breed breed = Breed.valueOf(breedName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour) && tmp.getBreed().equals(breed)){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    public List<KittyDTO> getOwnersCatsByColourAndBreed(String colourName, String breedName) {
        Owner owner = getCurrentOwner();
        Colour colour = Colour.valueOf(colourName);
        Breed breed = Breed.valueOf(breedName);
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : catRepository.findAll()){
            if (tmp.getColour().equals(colour) && tmp.getBreed().equals(breed) && tmp.getOwner().getId() == owner.getId()){
                cats.add(transformator.castDTO(tmp));
            }
        }
        return cats;
    }

    @Override
    public void makeFriends(int firstCatId, int secondCatId) {
        if (!catRepository.existsById(firstCatId)) {
            throw CatServiceException.noCat(firstCatId);
        }
        var firstCat = catRepository.getReferenceById(firstCatId);
        if (!catRepository.existsById(secondCatId)) {
            throw CatServiceException.noCat(secondCatId);
        }
        var secondCat = catRepository.getReferenceById(secondCatId);
        firstCat.makeFriend(secondCat);
        secondCat.makeFriend(firstCat);
        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    @Override
    public void unfriend(int firstCatId, int secondCatId) {
        if (!catRepository.existsById(firstCatId)) {
            throw CatServiceException.noCat(firstCatId);
        }
        var firstCat = catRepository.getReferenceById(firstCatId);
        if (!catRepository.existsById(secondCatId)) {
            throw CatServiceException.noCat(secondCatId);
        }
        var secondCat = catRepository.getReferenceById(secondCatId);
        firstCat.unfriend(secondCat);
        secondCat.unfriend(firstCat);
        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    public boolean checkOwner(int id){
        Owner tmp = getCurrentOwner();
        if (!catRepository.existsById(id)) {
            throw CatServiceException.noCat(id);
        }
        Cat cat = catRepository.getReferenceById(id);
        return cat.getOwner().getId() == tmp.getId();
    }

    private Owner getCurrentOwner(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()){
            throw CatServiceException.noUser(username);
        }
        return user.get().getOwner();
    }
}
