package com.rezarvasyon.saha.dto;

import com.rezarvasyon.saha.entity.Menu;
import com.rezarvasyon.saha.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long menuId;
    private Long restaurantId;
}
