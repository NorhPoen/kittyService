package org.ISKor.controller;

import jakarta.validation.Valid;
import org.ISKor.controller.startDTO.StartUserDTO;
import org.ISKor.dto.UserDTO;
import org.ISKor.service.UserServiceRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserControllerImpl implements UserController{
    private final UserServiceRepoImpl userService;

    @Autowired
    public UserControllerImpl(UserServiceRepoImpl userService) {
        this.userService = userService;
    }

    @PostMapping()
    @Override
    public UserDTO createUser(@Valid @RequestBody StartUserDTO userDTO) {
        return userService.createUser(userDTO.username(), userDTO.password(), userDTO.roleName(), userDTO.ownerId());
    }

    @KafkaListener(topics = "user_created", groupId = "user_group")
    public void listenUserCreated(UserDTO userDTO) {
        System.out.println("Received user created event: " + userDTO);
    }
}
