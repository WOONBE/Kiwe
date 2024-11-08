package com.d205.KIWI_Backend.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskSignOutRequest {
    private String password;
    private String refreshToken;


}
