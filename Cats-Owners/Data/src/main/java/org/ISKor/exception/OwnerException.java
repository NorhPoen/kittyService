package org.ISKor.exception;

public class OwnerException extends RuntimeException{
    private OwnerException(String message) {
        super(message);
    }
    public static OwnerException hasNotCat(int ownerId, int catId) {
        return new OwnerException("Owner with id " + ownerId + " has not cat with id " + catId);
    }
    public static OwnerException alreadyHasCat(int ownerId, int catId) {
        return new OwnerException("Owner with id " + ownerId + " already has cat with id " + catId);
    }
}
