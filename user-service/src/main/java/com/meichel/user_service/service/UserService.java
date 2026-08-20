package com.meichel.user_service.service;

import org.springframework.stereotype.Service;

import com.meichel.user_service.dto.UserDto;
import com.meichel.user_service.dto.UserResponse;
import com.meichel.user_service.dto.ProfileCreationResult;

import com.meichel.user_service.entity.User;
import com.meichel.user_service.expection.UserNotFoundException;
import com.meichel.user_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileCreationResult createProfileIfAbsent(String sub, String name, String email, UserDto request) {
        User existing = userRepository.findBySub(sub);
        if (existing != null) {
            return new ProfileCreationResult(toResponse(existing), false);
        }

        User created = User.builder()
                .sub(sub)
                .name(name)
                .email(email)
                .address(request.address())
                .enableAlerting(request.enableAlerting())
                .alertingThreshold(request.alertingThreshold())
                .build();

        return new ProfileCreationResult(toResponse(userRepository.save(created)), true);
    }

    public UserResponse getProfileBySub(String sub) {
        User user = userRepository.findBySub(sub);
        if (user == null) {
            throw new UserNotFoundException(
                    "Profile has not been created yet for sub: " + sub
                            + ". Please create your profile by calling POST /api/v1/users/ before accessing this endpoint.");
        }
        return toResponse(user);
    }

    public UserResponse updateProfileBySub(String sub, UserDto request) {
        User user = userRepository.findBySub(sub);
        if (user == null) {
            throw new UserNotFoundException("User with sub: " + sub + " not found.");
        }

        user.setAddress(request.address());
        user.setEnableAlerting(request.enableAlerting());
        user.setAlertingThreshold(request.alertingThreshold());

        return toResponse(userRepository.save(user));
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getSub(),
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.isEnableAlerting(),
                user.getAlertingThreshold());
    }
}
