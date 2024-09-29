package org.ISKor.controller;

import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.service.OwnerService;
import org.ISKor.controller.startDTO.StartOwnerDTO;

import java.util.List;

public class OwnerControllerImpl implements OwnerController {
    private final OwnerService service;

    public OwnerControllerImpl(OwnerService service) {
        this.service = service;
    }

    @Override
    public void createOwner(StartOwnerDTO ownerDTO) { service.createOwner(ownerDTO.name(), ownerDTO.birthDate()); }

    @Override
    public OwnerDTO getOwnerById(int id) {
        return service.getOwnerById(id);
    }

    @Override
    public List<OwnerDTO> getAllOwners() {
        return service.getAllOwners();
    }

    @Override
    public String deleteOwner(int id) {
        service.deleteOwner(id);
        return "Owner with id: " + id + " was successfully deleted";
    }

    @Override
    public List<KittyDTO> getAllCats(int id) {
        return service.getAllCats(id);
    }
}
