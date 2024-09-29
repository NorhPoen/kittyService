package org.ISKor.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.ISKor.dto.KittyListDTO;
import org.ISKor.dto.OwnerDTO;
import org.ISKor.dto.OwnerListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class OwnerProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactoryJson() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OwnerDTO> getOwner(ProducerFactory<String, OwnerDTO> producerFactoryJson) {
        return new KafkaTemplate<>(producerFactoryJson);
    }

    @Bean
    public KafkaTemplate<String, OwnerListDTO> getOwners(ProducerFactory<String, OwnerListDTO> producerFactoryJson) {
        return new KafkaTemplate<>(producerFactoryJson);
    }

    @Bean
    public KafkaTemplate<String, KittyListDTO> getKitties(ProducerFactory<String, KittyListDTO> producerFactoryJson) {
        return new KafkaTemplate<>(producerFactoryJson);
    }
}
