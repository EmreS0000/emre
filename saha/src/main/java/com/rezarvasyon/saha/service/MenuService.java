package com.rezarvasyon.saha.service;

import com.rezarvasyon.saha.dto.MenuItemDTO;
import com.rezarvasyon.saha.entity.Menu;
import com.rezarvasyon.saha.entity.MenuItem;
import com.rezarvasyon.saha.entity.Restaurant;
import com.rezarvasyon.saha.repository.MenuItemRepository;
import com.rezarvasyon.saha.repository.MenuRepository;
import com.rezarvasyon.saha.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

//    public Menu createMenuForRestaurant(Long restaurantId) {
//        Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(() -> new RuntimeException("Restaurant bulunamadı"));
//
//        if (menuRepository.findByRestaurantId(restaurantId).isPresent()) {
//            throw new RuntimeException("Bu restaurantın zaten bir menüsü var");
//        }
//
//        Menu menu = new Menu();
//        menu.setRestaurant(restaurant);
//        return menuRepository.save(menu);
//    }

    public MenuItemDTO addMenuItemToRestaurant(MenuItemDTO dto) {
        // Restaurant kontrolü
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant bulunamadı"));

        // Menü varsa al, yoksa oluştur
        Menu menu = menuRepository.findByRestaurantId(dto.getRestaurantId())
                .orElseGet(() -> {
                    Menu newMenu = new Menu();
                    newMenu.setRestaurant(restaurant);
                    return menuRepository.save(newMenu);
                });

        // MenuItem oluştur ve ilişkilendir
        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setMenu(menu);
        item.setRestaurant(restaurant);

        menu.getItems().add(item); // Menüye ekle
        menuRepository.save(menu); // Cascade ile item da kaydedilir

        // DTO dönüşü
        dto.setId(item.getId());
        dto.setMenuId(menu.getId());
        return dto;
    }


    public void deleteMenuItem(Long itemId) {
        menuItemRepository.deleteById(itemId);
    }

    public MenuItemDTO updateMenuItem(Long itemId, MenuItemDTO dto) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Menu item bulunamadı"));

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());

        return toDTO(menuItemRepository.save(item));
    }

    public List<MenuItemDTO> getMenuItemsByRestaurant(Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new RuntimeException("Menu bulunamadı"));

        return menu.getItems().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MenuItemDTO toDTO(MenuItem item) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setMenuId(item.getMenu().getId());
        dto.setRestaurantId(item.getRestaurant().getId());
        return dto;
    }
}
