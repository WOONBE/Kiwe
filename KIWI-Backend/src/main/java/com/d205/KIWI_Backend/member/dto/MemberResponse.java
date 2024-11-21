package com.d205.KIWI_Backend.member.dto;

import com.d205.KIWI_Backend.member.domain.Member;
import com.d205.KIWI_Backend.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Integer id;                // ID
    private String name;               // 이름
    private String email;              // 이메일
    private List<Integer> kioskIds;     // 키오스크 ID 목록
    private MemberType type;

    public static MemberResponse fromMember(Member member) {
        return MemberResponse.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .type(MemberType.USER)
            .kioskIds(member.getKiosks().stream()
                .map(kiosk -> kiosk.getId()) // 키오스크 ID 목록 생성
                .toList())
            .build();
    }
}
