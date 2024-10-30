package com.d205.KIWI_Backend.member.service;


import com.d205.KIWI_Backend.global.config.TokenProvider;
import com.d205.KIWI_Backend.member.domain.BlackList;
import com.d205.KIWI_Backend.member.repository.BlackListRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void signOut(String accessToken,String refreshToken) throws JsonProcessingException {

        tokenProvider.invalidateAccessToken(accessToken);
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
}
