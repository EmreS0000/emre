package com.rezarvasyon.saha.controller;

import com.rezarvasyon.saha.dto.LoginUserDto;
import com.rezarvasyon.saha.dto.LoginUserResponseDto;
import com.rezarvasyon.saha.dto.RegisterUserDto;
import com.rezarvasyon.saha.entity.User;
import com.rezarvasyon.saha.service.JwtService;
import com.rezarvasyon.saha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(
            JwtService jwtService,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = userService.registerUser(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> loginUser(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = userService.loginUser(loginUserDto);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", authenticatedUser.getRole().name());  // Role bilgisini ekliyoruz

        String jwtToken = jwtService.generateToken(extraClaims, authenticatedUser);

        LoginUserResponseDto loginUserResponseDto = new LoginUserResponseDto()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginUserResponseDto);
    }
}
