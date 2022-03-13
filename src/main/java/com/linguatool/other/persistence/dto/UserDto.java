package com.linguatool.other.persistence.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private LocalDateTime registeredDatetime;
}
