package org.ISKor.controller.startDTO;

import jakarta.validation.constraints.NotBlank;

public record StartUserDTO (@NotBlank(message = "Username can`t be empty") String username, @NotBlank(message = "Password can`t be empty") String password, @NotBlank(message = "Role can`t be empty") String roleName, int ownerId){
}
