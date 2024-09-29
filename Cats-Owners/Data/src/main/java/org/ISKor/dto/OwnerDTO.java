package org.ISKor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(force = true)
public class OwnerDTO {
    private final int id;
    private final String name;
    private final LocalDate birth;
    private final List<KittyDTO> cats;

    public OwnerDTO(int id, String name, LocalDate birth, List<KittyDTO> cats) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.cats = cats;
    }
}