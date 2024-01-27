package com.linguarium.configuration.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//kept in a separate class to avoid circular dependency problem
@Component
public class PasswordEncoder extends BCryptPasswordEncoder {

}
