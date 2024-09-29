package org.ISKor.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ISKor.exception.KittyException;
import org.ISKor.models.Breed;
import org.ISKor.models.Colour;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "Cats")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "breed")
    private Breed breed;
    @Column(name = "colour")
    private Colour colour;
    @Column(name = "birth")
    private LocalDate birth;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    private Owner owner;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cat> friends;

    public Cat(String name, Owner owner, Breed breed, Colour colour, LocalDate birth, List<Cat> friends) {
        this.name = name;
        this.owner = owner;
        this.breed = breed;
        this.colour = colour;
        this.birth = birth;
        this.friends = friends;
    }

    public void makeFriend(Cat friend) throws KittyException {
        Objects.requireNonNull(friend);
        for (Cat tmp : friends) {
            if (tmp.id == friend.id){
                throw KittyException.alreadyFriends(id, friend.getId());
            }
        }
        friends.add(friend);
    }

    public void unfriend(Cat friend) throws KittyException {
        Objects.requireNonNull(friend);
        for (Cat tmp : friends) {
            if (tmp.id == friend.id){
                friends.remove(friend);
                return;
            }
        }
        throw KittyException.noFriend(id, friend.id);
    }

}