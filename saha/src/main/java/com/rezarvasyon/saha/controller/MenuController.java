package com.rezarvasyon.saha.controller;

import com.rezarvasyon.saha.dto.MenuItemDTO;
import com.rezarvasyon.saha.entity.Menu;
import com.rezarvasyon.saha.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
//    @PostMapping("/create/{restaurantId}")
//    public ResponseEntity<Menu> createMenu(@PathVariable Long restaurantId) {
//        Menu createdMenu = menuService.createMenuForRestaurant(restaurantId);
//        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
//    }
    @GetMapping("/restaurant/{restaurantId}/items")
    public ResponseEntity<List<MenuItemDTO>> getMenuItems(@PathVariable Long restaurantId) {
        List<MenuItemDTO> items = menuService.getMenuItemsByRestaurant(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @PostMapping("/restaurant/{restaurantId}/addItem")
    public ResponseEntity<MenuItemDTO> addMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItemDTO dto) {
        dto.setRestaurantId(restaurantId);
        MenuItemDTO created = menuService.addMenuItemToRestaurant(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long itemId) {
        menuService.deleteMenuItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/items/{itemId}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long itemId, @RequestBody MenuItemDTO dto) {
        MenuItemDTO updated = menuService.updateMenuItem(itemId, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
