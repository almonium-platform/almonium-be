package com.linguatool.other;

import com.linguatool.linguatool.other.ObjectNotFoundException;
import com.linguatool.linguatool.exception.UserAlreadyExistAuthenticationException;
import com.linguatool.linguatool.mapper.UserMapper;
import com.linguatool.linguatool.persistence.dto.UserDto;
import com.linguatool.linguatool.persistence.entity.UserEntity;
import com.linguatool.linguatool.persistence.repository.UserRepository;
import com.linguatool.linguatool.other.ClockService;
import com.linguatool.linguatool.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ClockService clockService;

    @Transactional
    public UserDto createUser(final UserDto dto) {
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        final UserEntity newUser = userRepository.saveAndFlush(
                userMapper.dtoToEntity(dto, encodedPassword, clockService.getCurrentDateTimeUtc()));
        return userMapper.entityToDto(newUser);
    }

    @Transactional
    public UserDto registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        return null;
    }

    @Transactional
    public UserDto updateUser(final UserDto dto, final String email) {
        final UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("No user by email"));
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userMapper.updateUser(user, encodedPassword, dto);
        return userMapper.entityToDto(user);
    }

    //
    public UserDto getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new ObjectNotFoundException("No user by email"));
    }
}
