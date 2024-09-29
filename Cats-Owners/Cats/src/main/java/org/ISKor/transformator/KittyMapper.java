package org.ISKor.transformator;

import org.ISKor.dto.KittyDTO;
import org.ISKor.entity.Cat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KittyMapper {
    public KittyDTO castDTO(Cat cat){
        Objects.requireNonNull(cat);
        List<Integer> friends = new ArrayList<>();
        for (Cat tmpCat : cat.getFriends()) {
            friends.add(tmpCat.getId());
        }
        return new KittyDTO(cat.getId(), cat.getName(), cat.getOwner().getId(), cat.getBreed().name(), cat.getColour().name(), cat.getBirth(), friends);
    }
}
