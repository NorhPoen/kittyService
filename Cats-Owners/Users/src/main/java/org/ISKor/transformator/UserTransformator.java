package org.ISKor.transformator;

import org.ISKor.dto.UserDTO;
import org.ISKor.entity.User;

import java.util.Objects;

public class UserTransformator {
    public UserDTO castDTO(User user){
        Objects.requireNonNull(user);
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole().toString(), user.getOwner().getId());
    }
}
