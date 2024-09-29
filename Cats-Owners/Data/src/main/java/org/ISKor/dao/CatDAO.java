package org.ISKor.dao;

import org.ISKor.entity.Cat;

import java.util.List;

public interface CatDAO {
    Cat getCatById(int id);

    List<Cat> getAll();

    void addCat(Cat cat);

    void removeCat(Cat cat);

    void updateCat(Cat cat);

    List<Cat> getAllFriends(int id);
}
