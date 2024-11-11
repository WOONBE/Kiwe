package com.d205.KIWI_Backend.menu.repository;


import com.d205.KIWI_Backend.menu.domain.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByCategory(String category);

    @Query("SELECT COALESCE(MAX(m.categoryNumber), 0) FROM Menu m WHERE m.category = :category")
    Optional<Integer> findMaxCategoryNumberByCategory(String category);

}
