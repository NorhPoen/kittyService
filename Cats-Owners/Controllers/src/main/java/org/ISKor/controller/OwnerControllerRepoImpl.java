package org.ISKor.controller;

import jakarta.validation.Valid;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.ISKor.dto.KittyDTO;
import org.ISKor.dto.KittyListDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.dto.OwnerListDTO;
import org.ISKor.service.OwnerServiceRepoImpl;
import org.ISKor.controller.startDTO.StartOwnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/owner/")
public class OwnerControllerRepoImpl implements OwnerController{
    private final KafkaTemplate<String, StartOwnerDTO> createOwner;
    private final KafkaTemplate<String, Integer> getOwner;
    private final Consumer<String, OwnerDTO> consumer;
    private final Consumer<String, OwnerListDTO> consumerOwners;
    private final Consumer<String, KittyListDTO> consumerKitties;

    @Autowired
    public OwnerControllerRepoImpl(OwnerServiceRepoImpl service, KafkaTemplate<String, StartOwnerDTO> createOwner, KafkaTemplate<String, Integer> getOwner, Consumer<String, OwnerDTO> consumer, Consumer<String, OwnerListDTO> consumerOwners, @Qualifier("COsKF")  Consumer<String, KittyListDTO> consumerKitties) {
        this.createOwner = createOwner;
        this.getOwner = getOwner;
        this.consumer = consumer;
        this.consumerOwners = consumerOwners;
        this.consumerKitties = consumerKitties;
    }

    @Override
    @PostMapping()
    public void createOwner(@Valid @RequestBody StartOwnerDTO ownerDTO) {
        createOwner.send("create_owner", ownerDTO);
    }

    @Override
    @GetMapping("{id}")
    public OwnerDTO getOwnerById(@PathVariable("id") int id) throws InterruptedException {
        getOwner.send("get_by_id_owner", id);
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, OwnerDTO> consumerRecords = consumer.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, OwnerDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            OwnerDTO value = iterator.next().value();
            consumer.commitSync();
            return value;
        } else {
            return getOwnerById(id);
        }
    }

    @Override
    @GetMapping()
    public List<OwnerDTO> getAllOwners() throws InterruptedException {
        getOwner.send("get_owners", 0);
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, OwnerListDTO> consumerRecords = consumerOwners.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, OwnerListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            OwnerListDTO value = iterator.next().value();
            consumerOwners.commitSync();
            return value.owners();
        } else {
            return getAllOwners();
        }
    }

    @Override
    @DeleteMapping("{id}")
    public String deleteOwner(@PathVariable("id") int id) {
        getOwner.send("remove_owner", id);
        return "Owner with id: " + id + " was successfully deleted";
    }

    @Override
    @GetMapping("cats/{id}")
    public List<KittyDTO> getAllCats(@PathVariable("id") int id) throws InterruptedException {
        getOwner.send("get_by_id_owners_kitties", id);
        TimeUnit.MILLISECONDS.sleep(1000);
        ConsumerRecords<String, KittyListDTO> consumerRecords = consumerKitties.poll(Duration.ofMillis(2000));
        Iterator<ConsumerRecord<String, KittyListDTO>> iterator = consumerRecords.iterator();
        if (iterator.hasNext()) {
            KittyListDTO value = iterator.next().value();
            consumerKitties.commitSync();
            return value.cats();
        } else {
            return getAllCats(id);
        }
    }
}
