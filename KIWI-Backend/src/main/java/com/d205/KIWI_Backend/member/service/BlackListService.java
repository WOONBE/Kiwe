package com.d205.KIWI_Backend.member.service;


import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_CORRECT_PASSWORD;

import com.d205.KIWI_Backend.global.config.TokenProvider;
import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.member.domain.BlackList;
import com.d205.KIWI_Backend.member.domain.Member;
import com.d205.KIWI_Backend.member.repository.BlackListRepository;
import com.d205.KIWI_Backend.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void userSignOut(String accessToken,String refreshToken) throws JsonProcessingException {

        tokenProvider.invalidateAccessToken(accessToken);
        String cleanedRefreshToken = refreshToken.trim();
        validateAndBlacklistRefreshToken(cleanedRefreshToken);
    }

    @Transactional
    public void kioskSignOut(String password, String refreshToken) throws JsonProcessingException {
        // 현재 로그인한 사용자 ID 조회
        Integer memberId = getCurrentMemberId();
        System.out.println("memberId: " + memberId);

        // 사용자 ID로 사용자 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new JsonProcessingException("User not found with ID: " + memberId) {});

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadRequestException(ExceptionCode.INVALID_PASSWORD); // 비밀번호 불일치 예외 처리
        }

        // AccessToken 무효화
        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getType()));
        tokenProvider.invalidateAccessToken(accessToken);

        // RefreshToken 처리
        String cleanedRefreshToken = refreshToken.trim();
        validateAndBlacklistRefreshToken(cleanedRefreshToken);
    }


    private void validateAndBlacklistRefreshToken(String refreshToken) throws JsonProcessingException {
        // Refresh token이 블랙리스트에 존재하는지 확인
        if (blackListRepository.existsByInvalidRefreshToken(refreshToken)) {
            throw new JsonProcessingException("Refresh token is blacklisted.") {};  // JSON 처리 예외를 사용하여 표시
        }

        // Refresh token의 유효성 검사
        tokenProvider.validateAndParseToken(refreshToken);

        // 블랙리스트에 refreshToken 추가
        blackListRepository.save(new BlackList(refreshToken));
    }

    public Integer getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Integer.parseInt(userDetails.getUsername());
    }
}
