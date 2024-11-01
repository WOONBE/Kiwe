package com.d205.KIWI_Backend.log.repository;


import com.d205.KIWI_Backend.log.domain.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewCountRepository extends JpaRepository<ViewCount, Integer> {

    Boolean existsByRequestURI(String requestURI);
}
