package org.ISKor.service;

import org.ISKor.dto.KittyDTO;
import org.ISKor.exception.CatServiceException;

import java.time.LocalDate;
import java.util.List;

public interface CatService {
    KittyDTO createCat(String name, int ownerId, String breed, String colour, LocalDate birth) throws CatServiceException;
    void deleteCat(int id) throws CatServiceException;
    KittyDTO getCatById(int id);
    List<KittyDTO> getAllCats();
    List<KittyDTO> getAllFriends(int id);
    List<KittyDTO> getCatsByBreed(String breedName);
    List<KittyDTO> getCatsByColour(String colourName);
    List<KittyDTO> getCatsByColourAndBreed(String colourName, String breedName);
    void makeFriends(int firstCatId, int secondCatId);
    void unfriend(int firstCatId, int secondCatId);

}
