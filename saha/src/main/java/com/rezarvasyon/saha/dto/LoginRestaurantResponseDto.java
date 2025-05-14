package com.rezarvasyon.saha.dto;

public class LoginRestaurantResponseDto {

    public LoginRestaurantResponseDto setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginRestaurantResponseDto setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }


}