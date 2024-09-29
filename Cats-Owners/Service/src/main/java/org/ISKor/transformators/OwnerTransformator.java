package org.ISKor.transformators;

import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.entity.Cat;
import org.ISKor.entity.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OwnerTransformator {
    public OwnerDTO castDTO(Owner owner){
        Objects.requireNonNull(owner);
        List<KittyDTO> cats = new ArrayList<>();
        CatTransformator transformator = new CatTransformator();
        for (Cat cat : owner.getCats()) {
            cats.add(transformator.castDTO(cat));
        }
        return new OwnerDTO(owner.getId(), owner.getName(), owner.getBirth(), cats);
    }
}
