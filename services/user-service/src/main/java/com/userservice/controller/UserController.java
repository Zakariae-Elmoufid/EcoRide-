package com.userservice.controller;

import com.userservice.dto.DriverResponse;
import com.userservice.dto.UserRequest;
import com.userservice.dto.UserResponse;
import com.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt);
        return "ok";
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request,
                                                     @AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        UserResponse createdUser = userService.createUser(keycloakId,request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String keycloakId = jwt.getSubject();
        return ResponseEntity.ok(userService.getUserByKeycloakId(keycloakId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<DriverResponse> getDriverProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDriverProfile(id));
    }
}
