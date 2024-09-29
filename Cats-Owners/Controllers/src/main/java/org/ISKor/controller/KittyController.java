package org.ISKor.controller;

import org.ISKor.controller.startDTO.StartKittyDTO;
import org.ISKor.dto.KittyDTO;
import org.ISKor.exception.KittyException;

import java.util.List;

public interface KittyController {
    KittyDTO createCat(StartKittyDTO catDTO) throws KittyException;
    String deleteCat(int id) throws KittyException;
    KittyDTO getCatById(int id);
    List<KittyDTO> getAllCats();
    List<KittyDTO> getAllFriends(int id);
    List<KittyDTO> getCatsByBreed(String breedName);
    List<KittyDTO> getCatsByColour(String colourName);
    List<KittyDTO> getCatsByColourAndBreed(String colourName, String breedName);
    String makeFriends(int firstCatId, int secondCatId);
    String unfriend(int firstCatId, int secondCatId);
}
