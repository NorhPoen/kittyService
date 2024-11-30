package org.ISKor.exceptions;

public class KittyServiceException extends RuntimeException{
    private KittyServiceException(String message) {
        super(message);
    }
    public static KittyServiceException noOwner(int ownerId) {
        return new KittyServiceException("Owner with id " + ownerId + " not found");
    }
    public static KittyServiceException Kitty(int catId) {
        return new KittyServiceException("Kitty with id " + catId + " not found");
    }

    public static KittyServiceException noUser(String username) {
        return new KittyServiceException("User with username " + username + " not found");
    }
}
