package org.ISKor.service;

import org.ISKor.dto.OwnerDTO;
import org.ISKor.dto.StartOwnerDTO;

public interface OwnerService {
    OwnerDTO createOwner(StartOwnerDTO ownerCreateDto);
    void getOwnerById(int id);
    void findAllKitties(int id);
    void findAllOwners(int trash);
    void removeOwner(int id);
}
