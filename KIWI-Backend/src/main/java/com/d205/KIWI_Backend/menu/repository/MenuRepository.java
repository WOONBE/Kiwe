package com.d205.KIWI_Backend.menu.repository;


import com.d205.KIWI_Backend.menu.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByCategory(String category);

}
