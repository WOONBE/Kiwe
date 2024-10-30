package com.d205.KIWI_Backend.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskRequest {
    private String location; // 키오스크 위치
    private String status;   // 키오스크 상태
    private Integer ownerId; // 점주 ID (필요한 경우)
}
