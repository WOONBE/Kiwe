package com.d205.KIWI_Backend.member.repository;


import com.d205.KIWI_Backend.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    List<Member> findByName(String nickname);

}

