package com.rezarvasyon.saha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private Long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String address;
}
