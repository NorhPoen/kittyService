package org.ISKor.service;

import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;

import java.time.LocalDate;
import java.util.List;

public interface OwnerService {
    OwnerDTO createOwner(String name, LocalDate birth);
    OwnerDTO getOwnerById(int id);
    List<OwnerDTO> getAllOwners();
    void deleteOwner(int id);
    void addCat(int ownerId, int catId);
    List<KittyDTO> getAllCats(int id);
}
