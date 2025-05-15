package com.rezarvasyon.saha.controller;

import com.rezarvasyon.saha.dto.*;
import com.rezarvasyon.saha.entity.Restaurant;
import com.rezarvasyon.saha.entity.User;
import com.rezarvasyon.saha.service.JwtService;
import com.rezarvasyon.saha.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @GetMapping("/profile")
    public ResponseEntity<RestaurantDto> getRestaurantProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractUsername(token);
            Restaurant restaurant = restaurantService.getRestaurantByEmail(email);

            RestaurantDto dto = new RestaurantDto(
                    restaurant.getId(),
                    restaurant.getUsername(),
                    restaurant.getEmail(),
                    restaurant.getPhoneNumber(),
                    restaurant.getAddress()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();  // token hatalıysa 401
        }
    }
    @GetMapping("/getRestaurants")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        List<RestaurantDto> restaurantDtos = restaurants.stream()
                .map(r -> new RestaurantDto(
                        r.getId(),
                        r.getUsername(),
                        r.getEmail(),
                        r.getPhoneNumber(),
                        r.getAddress()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantDtos);
    }

}
