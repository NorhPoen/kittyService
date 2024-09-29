package org.ISKor.dto;

public record UserDTO(int id, String username, String password, String role, int ownerId) {
}
