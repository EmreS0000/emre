package com.rezarvasyon.saha.controller;

import com.rezarvasyon.saha.dto.*;
import com.rezarvasyon.saha.entity.Restaurant;
import com.rezarvasyon.saha.entity.User;
import com.rezarvasyon.saha.service.JwtService;
import com.rezarvasyon.saha.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {


    private final RestaurantService restaurantService;
    private final JwtService jwtService;

    public RestaurantController(RestaurantService restaurantService, JwtService jwtService) {
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
    }



    @PostMapping("/register")
    public ResponseEntity<Restaurant> registerRestaurant(@RequestBody RegisterRestaurantDto registerRestaurantDto) {
        Restaurant registeredRestaurant = restaurantService.registerRestaurant(registerRestaurantDto);
        return ResponseEntity.ok(registeredRestaurant);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginRestaurantResponseDto> loginRestaurant(@RequestBody LoginRestaurantDto loginRestaurantDto) {
        Restaurant authenticatedRestaurant = restaurantService.loginRestaurant(loginRestaurantDto);


        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", authenticatedRestaurant.getRole().name());  // Role bilgisini ekliyoruz


        String jwtToken = jwtService.generateToken(extraClaims, authenticatedRestaurant);





















        LoginRestaurantResponseDto loginRestaurantResponseDto = new LoginRestaurantResponseDto()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginRestaurantResponseDto);
    }


    @PostMapping("/addOrderItem")
    public ResponseEntity<String> addOrderItem(@RequestBody OrderItemDto orderItemDto) {
        try {
            restaurantService.addOrderItem(orderItemDto);
            return ResponseEntity.ok("Order item added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding order item: " + e.getMessage());
        }
    }
}
