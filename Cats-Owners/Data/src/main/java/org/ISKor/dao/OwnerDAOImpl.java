package org.ISKor.dao;

import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;

import java.util.List;

public class OwnerDAOImpl implements  OwnerDAO{
    @Override
    public Owner getOwnerById(int id) {
        return null;
    }

    @Override
    public List<Owner> getAll() {
        return null;
    }

    @Override
    public void addOwner(Owner owner) {

    }

    @Override
    public void removeOwner(Owner owner) {

    }

    @Override
    public void updateOwner(Owner owner) {

    }

    @Override
    public List<Cat> getAllCats(int id) {
        return null;
    }
    /*
    @Override
    public Owner getOwnerById(int id) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var owner = session.get(Owner.class, id);
        session.close();
        return owner;
    }

    @Override
    public List<Owner> getAll() {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var query = session.createQuery("FROM Owner", Owner.class);
        var owners = query.getResultList();
        session.close();
        return owners;
    }

    @Override
    public void addOwner(Owner owner) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.persist(owner);
        transaction.commit();
        session.close();
    }

    @Override
    public void removeOwner(Owner owner) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.remove(owner);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateOwner(Owner owner) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var transaction = session.beginTransaction();
        session.merge(owner);
        transaction.commit();
        session.close();
    }

    @Override
    public List<Cat> getAllCats(int id) {
        Session session = SessionFactorySingleton.getInstance().openSession();
        var query = session.createQuery("SELECT owner.cats FROM Owner owner WHERE owner.id=:id", Cat.class);
        query.setParameter("id", id);
        List<Cat> cats = query.getResultList();
        session.close();
        return cats;
    }
     */
}
