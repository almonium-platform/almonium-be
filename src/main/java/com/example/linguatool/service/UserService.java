package com.example.linguatool.service;

import com.example.linguatool.mapper.UserMapper;
import com.example.linguatool.persistence.dto.UserDto;
import com.example.linguatool.persistence.entity.UserEntity;
import com.example.linguatool.persistence.repository.UserRepository;
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
//
    @Transactional
    public UserDto createUser(final UserDto dto) {
        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
        final UserEntity newUser = userRepository.saveAndFlush(
                userMapper.dtoToEntity(dto, encodedPassword, clockService.getCurrentDateTimeUtc()));
        return userMapper.entityToDto(newUser);
    }
//
//    @Transactional
//    public UserDto updateUser(final UserDto dto, final String email) {
//        final UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ObjectNotFoundException("No user by email"));
//        final String encodedPassword = passwordEncoder.encode(dto.getPassword());
//        userMapper.updateUser(user, encodedPassword, dto);
//        return userMapper.mapToDto(user);
//    }
//
//    public UserDto getUserByEmail(final String email) {
//        return userRepository.findByEmail(email)
//                .map(userMapper::mapToDto)
//                .orElseThrow(() -> new ObjectNotFoundException("No user by email"));
//    }
}
