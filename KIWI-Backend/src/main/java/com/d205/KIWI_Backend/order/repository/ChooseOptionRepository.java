package com.d205.KIWI_Backend.order.repository;


import com.d205.KIWI_Backend.order.domain.ChooseOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChooseOptionRepository extends JpaRepository<ChooseOption, Long> {
}
