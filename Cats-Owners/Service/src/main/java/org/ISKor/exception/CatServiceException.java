package org.ISKor.exception;

public class CatServiceException extends  RuntimeException{
    private CatServiceException(String message) {
        super(message);
    }
    public static CatServiceException noOwner(int ownerId) {
        return new CatServiceException("Owner with id " + ownerId + " not found");
    }
    public static CatServiceException noCat(int catId) {
        return new CatServiceException("Cat with id " + catId + " not found");
    }

    public static CatServiceException noUser(String username) {
        return new CatServiceException("User with username " + username + " not found");
    }
}
