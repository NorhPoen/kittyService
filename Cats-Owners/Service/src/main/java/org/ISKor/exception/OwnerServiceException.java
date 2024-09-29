package org.ISKor.exception;

public class OwnerServiceException extends RuntimeException{
    private OwnerServiceException(String message) {
        super(message);
    }
    public static OwnerServiceException noOwner(int ownerId) {
        return new OwnerServiceException("Owner with id " + ownerId + " not found");
    }
    public static OwnerServiceException noCat(int catId) {
        return new OwnerServiceException("Cat with id " + catId + " not found");
    }
}
