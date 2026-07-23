package com.meichel.user_service.service;

import org.springframework.stereotype.Service;

import com.meichel.user_service.dto.UserDto;
import com.meichel.user_service.entity.User;
import com.meichel.user_service.expection.UserNotFoundException;
import com.meichel.user_service.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto input) {
        User createdUser = User.builder()
                .firstName(input.firstName())
                .lastName(input.lastName())
                .email(input.email())
                .address(input.address())
                .enableAlerting(input.enableAlerting())
                .alertingThreshold(input.alertingThreshold())
                .build();

        User saved = userRepository.save(createdUser);
        return toDto(saved);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return toDto(user);
    }

    public UserDto updateUser(Long id, UserDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setAddress(request.address());
        user.setEmail(request.email());
        user.setEnableAlerting(request.enableAlerting());
        user.setAlertingThreshold(request.alertingThreshold());

        User updated = userRepository.save(user);
        return toDto(updated);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getEmail(),
                user.isEnableAlerting(),
                user.getAlertingThreshold());
    }
}
