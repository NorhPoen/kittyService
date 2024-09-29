package org.ISKor.dao;

import org.ISKor.entity.Cat;

import java.util.List;

public class CatDAOImpl implements CatDAO{
    @Override
    public Cat getCatById(int id) {
        return null;
    }

    @Override
    public List<Cat> getAll() {
        return null;
    }

    @Override
    public void addCat(Cat cat) {

    }

    @Override
    public void removeCat(Cat cat) {

    }

    @Override
    public void updateCat(Cat cat) {

    }

    @Override
    public List<Cat> getAllFriends(int id) {
        return null;
    }
    /*
    @Override
    public Cat getCatById(int id) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var cat = session.get(Cat.class, id);
        session.close();
        return cat;
    }

    @Override
    public List<Cat> getAll() {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var query = session.createQuery("FROM Cat", Cat.class);
        var cats = query.getResultList();
        session.close();
        return cats;
    }

    @Override
    public void addCat(Cat cat) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.persist(cat);
        transaction.commit();
        session.close();
    }

    @Override
    public void removeCat(Cat cat) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.remove(cat);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateCat(Cat cat) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.merge(cat);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Cat> getAllFriends(int id) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var query = session.createQuery("SELECT cat.friends FROM Cat cat WHERE cat.id=:id", Cat.class);
        query.setParameter("id", id);
        List<Cat> friends = query.getResultList();
        session.close();
        return friends;
    }

     */
}
