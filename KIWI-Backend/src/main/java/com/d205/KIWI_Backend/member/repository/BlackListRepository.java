package com.d205.KIWI_Backend.member.repository;

import com.d205.KIWI_Backend.member.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    boolean existsByInvalidRefreshToken(String refreshToken);
}
