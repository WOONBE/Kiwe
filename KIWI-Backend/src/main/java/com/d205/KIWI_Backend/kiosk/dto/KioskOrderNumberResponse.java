package com.d205.KIWI_Backend.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskOrderNumberResponse {
    private Long kioskId;           // 키오스크 ID
    private Long kioskOrderNumber;      // 매장의 키오스크 순서
}
