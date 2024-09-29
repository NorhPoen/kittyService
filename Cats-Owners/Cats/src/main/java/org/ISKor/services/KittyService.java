package org.ISKor.services;

import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.KittyFriendsDTO;
import org.ISKor.dto.FilterDTO;
import org.ISKor.dto.StartKittyDTO;

public interface KittyService {
    KittyDTO createKitty(StartKittyDTO kittyCreateDto);
    void makeFriends(KittyFriendsDTO friendsDto);
    void unfriendKitties(KittyFriendsDTO friendsDto);
    void getKittyById(int id);
    //void findAllKitties(int trash);
    void findAllFriends(int id);
    void removeKitty(int id);
    void findKittiesByBreed(FilterDTO filterDto);
    void findKittiesByColour(FilterDTO filterDto);
    void findKittiesByColourAndBreed(FilterDTO filterDto);

}