package com.d205.KIWI_Backend.kiosk.repository;

import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskRepository extends JpaRepository<Kiosk, Integer> {
    // 필요한 쿼리 메서드를 추가할 수 있습니다.
}
