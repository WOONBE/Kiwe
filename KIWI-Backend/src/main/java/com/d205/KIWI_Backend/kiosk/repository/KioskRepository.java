package com.d205.KIWI_Backend.kiosk.repository;

import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.member.domain.Member;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskRepository extends JpaRepository<Kiosk, Integer> {

    List<Kiosk> findByMember(Member member);
    // 필요한 쿼리 메서드를 추가할 수 있습니다.
}
