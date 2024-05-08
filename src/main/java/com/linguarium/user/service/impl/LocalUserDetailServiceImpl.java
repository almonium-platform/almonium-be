package com.linguarium.user.service.impl;

import com.linguarium.user.model.User;
import com.linguarium.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalUserDetailServiceImpl implements UserDetailsService {
    UserService userService;

    @Override
    @Transactional
    public User loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userService
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found in the database"));
    }

    @Transactional
    public User loadUserById(Long id) {
        return userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("User not found " + id));
    }
}
