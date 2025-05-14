package com.rezarvasyon.saha.repository;

import com.rezarvasyon.saha.entity.Menu;
import com.rezarvasyon.saha.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
   Optional<Menu> findByRestaurantId(Long restaurantId);
}
