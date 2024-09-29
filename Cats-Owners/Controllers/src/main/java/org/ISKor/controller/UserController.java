package org.ISKor.controller;

import org.ISKor.controller.startDTO.StartUserDTO;
import org.ISKor.dto.UserDTO;

public interface UserController {
    public UserDTO createUser(StartUserDTO userDTO);
}
