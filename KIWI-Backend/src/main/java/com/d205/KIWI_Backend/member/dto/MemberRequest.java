package com.d205.KIWI_Backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String name;               // 이름
    private String email;              // 이메일
    private String password;            // 비밀번호
    private List<Integer> kioskIds;     // 키오스크 ID 목록
}
