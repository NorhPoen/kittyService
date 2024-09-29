package org.ISKor.controller;

import jakarta.validation.Valid;
import org.ISKor.controller.startDTO.StartKittyDTO;
import org.ISKor.dto.FilterDTO;
import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.KittyFriendsDTO;
import org.ISKor.dto.KittyListDTO;
import org.ISKor.entity.Owner;
import org.ISKor.entity.User;
import org.ISKor.exception.CatServiceException;
import org.ISKor.repositories.UserRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/kitties/")
public class KittyControllerRepoImpl {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, StartKittyDTO> createKitty;
    private final KafkaTemplate<String, Integer> getKitty;
    private final KafkaTemplate<String, KittyFriendsDTO> friends;
    private final KafkaTemplate<String, FilterDTO> filters;
    private final Consumer<String, KittyListDTO> consumerKitties;
    private final Consumer<String, KittyListDTO> consumerFriends;
    private final Consumer<String, KittyListDTO> consumerFiltered;
    private final Consumer<String, KittyDTO> consumer;

    @Autowired
    public KittyControllerRepoImpl(
            UserRepository userRepository, KafkaTemplate<String, StartKittyDTO> createKitty,
            KafkaTemplate<String, Integer> getKitty,
            KafkaTemplate<String, KittyFriendsDTO> friends,
            KafkaTemplate<String, FilterDTO> filters,
            @Qualifier("CKF") Consumer<String, KittyListDTO> consumerKitties,
            @Qualifier("CKFF") Consumer<String, KittyListDTO> consumerFriends,
            @Qualifier("CFF")  Consumer<String, KittyListDTO> consumerFiltered,
            Consumer<String, KittyDTO> consumer) {
        this.userRepository = userRepository;
        this.createKitty = createKitty;
        this.getKitty = getKitty;
        this.friends = friends;
        this.filters = filters;
        this.consumerKitties = consumerKitties;
        this.consumerFriends = consumerFriends;
        this.consumerFiltered = consumerFiltered;
        this.consumer = consumer;
    }


    @PostMapping()
    public void createCat(@Valid @RequestBody StartKittyDTO catDTO) throws CatServiceException {
        createKitty.send("create_kitty", catDTO);
    }


    @DeleteMapping("/{id}")
    public String deleteCat(@PathVariable("id") int id) throws CatServiceException {
        getKitty.send("remove_kitty", id);
        return "Cat with id: " + id + " was successfully deleted";
    }


    @GetMapping("{id}")
    public KittyDTO getCatById(@PathVariable("id") int id) throws InterruptedException {

        getKitty.send("get_by_id_kitty", id);
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyDTO> consumerRecords = consumer.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyDTO value = iterator.next().value();
            consumer.commitSync();
            return value;
        } else {
            return getCatById(id);
        }
    }

    @GetMapping()
    public List<KittyDTO> getAllCats() throws InterruptedException {
        if (checkAdmin()) {
            getKitty.send("get_kitties_admin", 0);
        } else {
            Owner owner = getCurrentOwner();
            getKitty.send("get_kitties_by_owner", owner.getId());
        }
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerKitties.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerKitties.commitSync();
            return value.cats();
        } else {
            return getAllCats();
        }
    }

    private boolean checkAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    private Owner getCurrentOwner() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get().getOwner();
    }

    @GetMapping("friends/{id}")
    public List<KittyDTO> getAllFriends(@PathVariable("id") int id) throws InterruptedException {
        getKitty.send("get_by_id_friends", id);
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerFriends.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerFriends.commitSync();
            return value.cats();
        } else {
            return getAllFriends(id);
        }
    }


    @GetMapping("getBreed")
    public List<KittyDTO> getCatsByBreed(@RequestParam(name = "breed") String breedName) throws InterruptedException {
        filters.send("get_kitties_by_breed", new FilterDTO(breedName, null));
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerFiltered.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerFiltered.commitSync();
            return value.cats();
        } else {
            return getCatsByBreed(breedName);
        }
    }


    @GetMapping("getColour")
    public List<KittyDTO> getCatsByColour(@RequestParam(name = "colour") String colourName) throws InterruptedException {
        filters.send("get_kitties_by_colour", new FilterDTO(null, colourName));
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerFiltered.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerFiltered.commitSync();
            return value.cats();
        } else {
            return getCatsByBreed(colourName);
        }
    }


    @GetMapping("getColourBreed")
    public List<KittyDTO> getCatsByColourAndBreed(@RequestParam(name = "colour") String colourName, @RequestParam(name = "breed") String breedName) throws InterruptedException {
        filters.send("get_kitties_by_filters", new FilterDTO(breedName, colourName));
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerFiltered.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerFiltered.commitSync();
            return value.cats();
        } else {
            return getCatsByBreed(breedName);
        }
    }


    @PutMapping("makeFriends")
    public String makeFriends(@RequestParam(name = "cat1") int firstCatId, @RequestParam(name = "cat2") int secondCatId) {
        friends.send("befriend", new KittyFriendsDTO(firstCatId, secondCatId));
        return "Cat with id: " + firstCatId + " and cat with id: " + secondCatId + " now are friends";
    }


    @PutMapping("unfriend")
    public String unfriend(@RequestParam(name = "cat1") int firstCatId, @RequestParam(name = "cat2") int secondCatId) {
        friends.send("unfriend", new KittyFriendsDTO(firstCatId, secondCatId));
        return "Cat with id: " + firstCatId + " and cat with id: " + secondCatId + " now aren`t friends";
    }
}
