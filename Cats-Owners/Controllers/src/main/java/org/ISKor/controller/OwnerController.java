package org.ISKor.controller;

import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.controller.startDTO.StartOwnerDTO;

import java.util.List;

public interface OwnerController {
    void createOwner(StartOwnerDTO ownerDTO);
    OwnerDTO getOwnerById(int id) throws InterruptedException;
    List<OwnerDTO> getAllOwners() throws InterruptedException;
    String deleteOwner(int id);
    List<KittyDTO> getAllCats(int id) throws InterruptedException;
}
