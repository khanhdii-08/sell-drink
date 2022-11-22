package com.example.CartService.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Collection<RoleDto> roles = new ArrayList<>();
}
