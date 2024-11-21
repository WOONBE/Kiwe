package com.d205.KIWI_Backend.member.dto;

import com.d205.KIWI_Backend.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
        private String email;
        private MemberType type;
        private String accessToken;	// token -> accessToken으로 액세스 토큰임을 명시
        private String refreshToken;
}