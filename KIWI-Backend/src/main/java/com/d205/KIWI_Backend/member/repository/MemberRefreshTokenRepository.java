package com.d205.KIWI_Backend.member.repository;


import com.d205.KIWI_Backend.member.domain.Member;
import com.d205.KIWI_Backend.member.domain.MemberRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Integer> {
    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(Integer memberId,
        long count);

//    Optional<MemberRefreshToken> findByMemberId(Integer id);
    Optional<MemberRefreshToken> findByMember(Member member);
}