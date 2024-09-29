package org.ISKor.exception;

public class KittyException extends RuntimeException{
    private KittyException(String message) {
        super(message);
    }
    public static KittyException noFriend(int catId, int friendId) {
        return new KittyException("Cat with id " + catId + " and cat with id " + friendId + " are not friends");
    }
    public static KittyException alreadyFriends(int catId, int friendId) {
        return new KittyException("Cat with id " + catId + " and cat with id " + friendId + "are already friends");
    }

}
