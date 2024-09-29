package org.ISKor.services;

import org.ISKor.dto.UserDTO;

public interface UserService {
    UserDTO createUser(String username, String password, String roleName, int ownerId);
}
