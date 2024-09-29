package org.ISKor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ISKor.exception.OwnerException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "birth")
    private LocalDate birth;
    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cat> cats;

    public Owner(String name, LocalDate birth, List<Cat> cats) {
        this.name = name;
        this.birth = birth;
        this.cats = cats;
    }

    public void addCat(Cat cat) throws OwnerException {
        Objects.requireNonNull(cat);
        if (cats.contains(cat)) {
            throw OwnerException.alreadyHasCat(id, cat.getId());
        }
        cats.add(cat);
    }

    public void deleteCat(Cat cat) throws OwnerException {
        Objects.requireNonNull(cat);
        if (!cats.contains(cat)) {
            throw OwnerException.hasNotCat(id, cat.getId());
        }
        cats.remove(cat);
    }

}