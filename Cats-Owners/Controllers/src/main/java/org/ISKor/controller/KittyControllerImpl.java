package org.ISKor.controller;

import org.ISKor.controller.startDTO.StartKittyDTO;
import org.ISKor.dto.KittyDTO;
import org.ISKor.exception.KittyException;
import org.ISKor.service.CatService;

import java.util.List;

public class KittyControllerImpl implements KittyController {
    private final CatService service;

    public KittyControllerImpl(CatService service) {
        this.service = service;
    }

    @Override
    public KittyDTO createCat(StartKittyDTO catDTO) throws KittyException {
        return service.createCat(catDTO.name(), catDTO.ownerId(), catDTO.breed(), catDTO.colour(), catDTO.birthDate());
    }

    @Override
    public String deleteCat(int id) throws KittyException {
     service.deleteCat(id);
     return "Cat with id: " + id + " was successfully deleted";
    }

    @Override
    public KittyDTO getCatById(int id) {
        return service.getCatById(id);
    }

    @Override
    public List<KittyDTO> getAllCats() {
        return service.getAllCats();
    }

    @Override
    public List<KittyDTO> getAllFriends(int id) {
        return service.getAllCats();
    }

    @Override
    public List<KittyDTO> getCatsByBreed(String breedName) { return service.getCatsByBreed(breedName); }

    @Override
    public List<KittyDTO> getCatsByColour(String colourName) { return service.getCatsByColour(colourName); }

    @Override
    public List<KittyDTO> getCatsByColourAndBreed(String colourName, String breedName) { return service.getCatsByColourAndBreed(colourName, breedName); }

    @Override
    public String makeFriends(int firstCatId, int secondCatId) {
        service.makeFriends(firstCatId, secondCatId);
        return "Cat with id: " + firstCatId + " and cat with id: " + secondCatId + " now are friends";
    }

    @Override
    public String unfriend(int firstCatId, int secondCatId) {
        service.unfriend(firstCatId,secondCatId);
        return "Cat with id: " + firstCatId + " and cat with id: " + secondCatId + " now aren`t friends";
    }
}
