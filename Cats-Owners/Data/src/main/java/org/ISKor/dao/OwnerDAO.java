package org.ISKor.dao;


import org.ISKor.entity.Owner;
import org.ISKor.entity.Cat;

import java.util.List;

public interface OwnerDAO {

    Owner getOwnerById(int id);

    List<Owner> getAll();

    void addOwner(Owner owner);

    void removeOwner(Owner owner);

    void updateOwner(Owner owner);

    List<Cat> getAllCats(int id);
}
