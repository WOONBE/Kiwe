package com.d205.KIWI_Backend.kiosk.dto;

import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskResponse {
    private Integer id;           // 키오스크 ID
    private String location;      // 키오스크 위치
    private String status;        // 키오스크 상태

    public static KioskResponse fromKiosk(Kiosk kiosk) {
        return KioskResponse.builder()
                .id(kiosk.getId())
                .location(kiosk.getLocation())
                .status(kiosk.getStatus())
                .build();
    }
}
