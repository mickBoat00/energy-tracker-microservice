package com.meichel.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meichel.user_service.dto.UserDto;
import com.meichel.user_service.dto.UserResponse;
import com.meichel.user_service.dto.ProfileCreationResult;

import com.meichel.user_service.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<UserResponse> createProfile(
            @RequestHeader(value = "X-User-Sub", required = false) String sub,
            @RequestHeader(value = "X-User-Name", required = false) String name,
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @RequestBody UserDto request) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProfileCreationResult result = userService.createProfileIfAbsent(sub, name, email, request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(result.profile());
    }

    @GetMapping("/me/")
    public ResponseEntity<UserResponse> getMyProfile(
            @RequestHeader(value = "X-User-Sub", required = false) String sub) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getProfileBySub(sub));
    }

    @PutMapping("/me/")
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestHeader(value = "X-User-Sub", required = false) String sub,
            @RequestBody UserDto request) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.updateProfileBySub(sub, request));
    }

    @GetMapping("/sub/{sub}/")
    public ResponseEntity<UserResponse> getUserBySub(@PathVariable String sub) {
        return ResponseEntity.ok(userService.getProfileBySub(sub));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
