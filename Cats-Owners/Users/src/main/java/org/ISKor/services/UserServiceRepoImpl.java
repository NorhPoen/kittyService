package org.ISKor.services;

import org.ISKor.dto.UserDTO;
import org.ISKor.entity.Owner;
import org.ISKor.entity.User;
import org.ISKor.exception.UserServiceException;
import org.ISKor.models.Role;
import org.ISKor.repositories.OwnerRepository;
import org.ISKor.repositories.UserRepository;
import org.ISKor.transformator.UserTransformator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceRepoImpl implements UserService{
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final KafkaTemplate<String, UserDTO> kafkaTemplate;

    @Autowired
    public UserServiceRepoImpl(UserRepository userRepository, OwnerRepository ownerRepository,
                               KafkaTemplate<String, UserDTO> kafkaTemplate) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public UserDTO createUser(String username, String password, String roleName, int ownerId) {
        if(!ownerRepository.existsById(ownerId)){
            throw UserServiceException.noOwner(ownerId);
        }
        Owner owner = ownerRepository.getReferenceById(ownerId);
        if(userRepository.findUserByUsername(username).isPresent()){
            throw UserServiceException.usernameAlreadyExist(username);
        }
        if (userRepository.findUserByOwner(owner).isPresent()){
            throw UserServiceException.ownerRegistered(ownerId);
        }
        Role role;
        if (Objects.equals(roleName, Role.ADMIN.name())){
            role = Role.ADMIN;
        } else if (Objects.equals(roleName, Role.USER.name())) {
            role = Role.USER;
        } else {
            throw UserServiceException.noRole(roleName);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User tmp = new User(username, encoder.encode(password), role, owner);
        userRepository.save(tmp);

        // Преобразование пользователя в DTO
        UserTransformator transformator = new UserTransformator();
        UserDTO userDTO = transformator.castDTO(tmp);

        // Отправка сообщения в Kafka после создания пользователя
        kafkaTemplate.send("user_created", userDTO);

        return userDTO;
    }
}
