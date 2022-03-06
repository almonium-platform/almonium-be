package com.example.linguatool.persistence.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
