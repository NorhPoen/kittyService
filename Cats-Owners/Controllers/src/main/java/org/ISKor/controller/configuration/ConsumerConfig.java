package org.ISKor.controller.configuration;

import org.ISKor.dto.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Consumer for Users
    @Bean
    public ConsumerFactory<String, UserDTO> consumerUserFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public Consumer<String, UserDTO> createUserFactory(ConsumerFactory<String, UserDTO> consumerUserFactory) {
        Consumer<String, UserDTO> consumer = consumerUserFactory.createConsumer("groupIdUser", "clientIdUser");
        consumer.subscribe(List.of("user_created"));
        return consumer;
    }


    @Bean
    public ConsumerFactory<String, OwnerDTO> consumerOwnerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OwnerDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public Consumer<String, OwnerDTO> createOwnerFactory(ConsumerFactory<String, OwnerDTO> consumerOwnerFactory) {
        Consumer<String, OwnerDTO> consumer = consumerOwnerFactory.createConsumer("groupIdCOF", "clientIdCOF");
        consumer.subscribe(List.of("got_by_id_owner"));
        return consumer;
    }

    @Bean
    public ConsumerFactory<String, OwnerListDTO> consumerOwnersFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OwnerListDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public Consumer<String, OwnerListDTO> createOwnersFactory(ConsumerFactory<String, OwnerListDTO> consumerOwnersFactory) {
        Consumer<String, OwnerListDTO> consumer = consumerOwnersFactory.createConsumer("groupIdCOsF", "clientIdCOsF");
        consumer.subscribe(List.of("got_owners"));
        return consumer;
    }

    @Bean
    public ConsumerFactory<String, KittyListDTO> consumerKittiesFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KittyListDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "COsKF")
    public Consumer<String, KittyListDTO> createOwnersKittiesFactory(ConsumerFactory<String, KittyListDTO> consumerKittiesFactory) {
        Consumer<String, KittyListDTO> consumer = consumerKittiesFactory.createConsumer("groupIdCOsKF", "clientIdCOsKF");
        consumer.subscribe(List.of("got_by_id_owners_kitties"));
        return consumer;
    }

    @Bean(name = "CKFF")
    public Consumer<String, KittyListDTO> createKittiesFriendsFactory(ConsumerFactory<String, KittyListDTO> consumerKittiesFactory) {
        Consumer<String, KittyListDTO> consumer = consumerKittiesFactory.createConsumer("groupIdCKFF", "clientIdCKFF");
        consumer.subscribe(List.of("got_by_id_friends"));
        return consumer;
    }

    @Bean(name = "CKF")
    public Consumer<String, KittyListDTO> createKittiesFactory(ConsumerFactory<String, KittyListDTO> consumerKittiesFactory) {
        Consumer<String, KittyListDTO> consumer = consumerKittiesFactory.createConsumer("groupIdCKF", "clientIdCKF");
        consumer.subscribe(List.of("got_kitties"));
        return consumer;
    }

    @Bean(name = "CFF")
    public Consumer<String, KittyListDTO> createFilteredFactory(ConsumerFactory<String, KittyListDTO> consumerKittiesFactory) {
        Consumer<String, KittyListDTO> consumer = consumerKittiesFactory.createConsumer("groupIdCFF", "clientIdCFF");
        consumer.subscribe(List.of("got_kitties_by_filters"));
        return consumer;
    }

    @Bean
    public ConsumerFactory<String, KittyDTO> consumerKittyFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KittyDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public Consumer<String, KittyDTO> createKittyFactory(ConsumerFactory<String, KittyDTO> consumerKittyFactory) {
        Consumer<String, KittyDTO> consumer = consumerKittyFactory.createConsumer("groupId", "clientId");
        consumer.subscribe(List.of("got_by_id_kitty"));
        return consumer;
    }
}
