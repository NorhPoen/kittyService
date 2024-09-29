package org.ISKor.service;

import org.ISKor.dao.CatDAO;
import org.ISKor.dao.OwnerDAO;
import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;
import org.ISKor.exception.OwnerServiceException;
import org.ISKor.transformators.CatTransformator;
import org.ISKor.transformators.OwnerTransformator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerServiceImpl implements  OwnerService{
    private final CatDAO catDAO;
    private final OwnerDAO ownerDAO;

    public OwnerServiceImpl(CatDAO catDAO, OwnerDAO ownerDAO) {
        this.catDAO = catDAO;
        this.ownerDAO = ownerDAO;
    }

    @Override
    public OwnerDTO createOwner(String name, LocalDate birth) {
        var owner = new Owner(name, birth, new ArrayList<>());
        ownerDAO.addOwner(owner);
        var transformator = new OwnerTransformator();
        return transformator.castDTO(owner);
    }

    @Override
    public OwnerDTO getOwnerById(int id) {
        var owner = ownerDAO.getOwnerById(id);
        if (owner == null){
            throw OwnerServiceException.noOwner(id);
        }
        var transformator = new OwnerTransformator();
        return transformator.castDTO(owner);
    }

    @Override
    public List<OwnerDTO> getAllOwners() {
        var owners = new ArrayList<OwnerDTO>();
        var transformator = new OwnerTransformator();
        for (Owner owner : ownerDAO.getAll()){
            owners.add(transformator.castDTO(owner));
        }
        return owners;
    }

    @Override
    public void deleteOwner(int id) {
        var owner = ownerDAO.getOwnerById(id);
        if (owner == null){
            throw OwnerServiceException.noOwner(id);
        }
        ownerDAO.removeOwner(owner);
    }

    @Override
    public void addCat(int ownerId, int catId) {
        var owner = ownerDAO.getOwnerById(ownerId);
        if (owner == null){
            throw OwnerServiceException.noOwner(ownerId);
        }
        var cat = catDAO.getCatById(catId);
        if (cat == null){
            throw OwnerServiceException.noCat(catId);
        }
        owner.addCat(cat);
        cat.setOwner(owner);
        ownerDAO.updateOwner(owner);
        catDAO.updateCat(cat);
    }

    @Override
    public List<KittyDTO> getAllCats(int id) {
        var owner = ownerDAO.getOwnerById(id);
        if (owner == null){
            throw OwnerServiceException.noOwner(id);
        }
        var cats = new ArrayList<KittyDTO>();
        var transformator = new CatTransformator();
        for (Cat tmp : ownerDAO.getAllCats(id)){
            cats.add(transformator.castDTO(tmp));
        }
        return cats;
    }
}
