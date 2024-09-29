package org.ISKor.service;

import org.ISKor.dto.UserDTO;
import org.ISKor.entity.Owner;
import org.ISKor.entity.User;
import org.ISKor.exception.UserServiceException;
import org.ISKor.models.Role;
import org.ISKor.repositories.OwnerRepository;
import org.ISKor.repositories.UserRepository;
import org.ISKor.transformators.UserTransformator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceRepoImpl implements UserService{
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public UserServiceRepoImpl(UserRepository userRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
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
        UserTransformator transformator = new UserTransformator();
        return transformator.castDTO(tmp);
    }
}
