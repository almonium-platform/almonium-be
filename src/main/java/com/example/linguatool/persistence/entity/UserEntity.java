package com.example.linguatool.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(250)")
    private String name;
    @Column(name = "email", nullable = false, columnDefinition = "varchar(100)", unique = true, updatable = false)
    private String email;
    @Column(name = "username", nullable = false, columnDefinition = "varchar(30)")
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "registered_datetime", nullable = false, updatable = false)
    private LocalDateTime registeredDatetime;

}
