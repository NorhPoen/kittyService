package org.ISKor.controller.configuration;

import org.ISKor.controller.startDTO.StartKittyDTO;
import org.ISKor.controller.startDTO.StartOwnerDTO;
import org.ISKor.dto.FilterDTO;
import org.ISKor.dto.KittyFriendsDTO;
import org.ISKor.dto.UserDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, UserDTO> producerFactoryUser() {
        return new DefaultKafkaProducerFactory<>(getPropsJson());
    }

    @Bean
    public KafkaTemplate<String, UserDTO> createUser(ProducerFactory<String, UserDTO> producerFactoryUser) {
        return new KafkaTemplate<>(producerFactoryUser);
    }

    @Bean
    public ProducerFactory<String, StartOwnerDTO> producerFactoryOwner() {
        return new DefaultKafkaProducerFactory<>(getPropsJson());
    }

    @Bean
    public KafkaTemplate<String, StartOwnerDTO> createOwner(ProducerFactory<String, StartOwnerDTO> producerFactoryOwner) {
        return new KafkaTemplate<>(producerFactoryOwner);
    }

    @Bean
    public ProducerFactory<String, StartKittyDTO> producerFactoryKitty() {
        return new DefaultKafkaProducerFactory<>(getPropsJson());
    }

    @Bean
    public KafkaTemplate<String, StartKittyDTO> createKitty(ProducerFactory<String, StartKittyDTO> producerFactoryKitty) {
        return new KafkaTemplate<>(producerFactoryKitty);
    }

    @Bean
    public ProducerFactory<String, KittyFriendsDTO> producerFactoryFriends() {
        return new DefaultKafkaProducerFactory<>(getPropsJson());
    }

    @Bean
    public KafkaTemplate<String, KittyFriendsDTO> createFriends(ProducerFactory<String, KittyFriendsDTO> producerFactoryFriends) {
        return new KafkaTemplate<>(producerFactoryFriends);
    }

    @Bean
    public ProducerFactory<String, FilterDTO> producerFactoryFilters() {
        return new DefaultKafkaProducerFactory<>(getPropsJson());
    }

    @Bean
    public KafkaTemplate<String, FilterDTO> createFilters(ProducerFactory<String, FilterDTO> producerFactoryFilters) {
        return new KafkaTemplate<>(producerFactoryFilters);
    }

    @Bean
    public ProducerFactory<String, Integer> producerFactoryById() {
        return new DefaultKafkaProducerFactory<>(getPropsInteger());
    }
    @Bean
    public KafkaTemplate<String, Integer> getOwner(ProducerFactory<String, Integer> producerFactoryById) {
        return new KafkaTemplate<>(producerFactoryById);
    }

    private Map<String, Object> getPropsJson(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    private Map<String, Object> getPropsInteger(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }
}
