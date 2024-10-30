package com.d205.KIWI_Backend.kiosk.controller;

import com.d205.KIWI_Backend.kiosk.dto.KioskRequest;
import com.d205.KIWI_Backend.kiosk.dto.KioskResponse;
import com.d205.KIWI_Backend.kiosk.service.KioskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kiosks")
@RequiredArgsConstructor
public class KioskController {

    private final KioskService kioskService;

    // 키오스크 생성
    @PostMapping
    @Operation(summary = "키오스크 생성", description = "키오스크를 생성하는 API")
    public ResponseEntity<KioskResponse> createKiosk(@RequestBody KioskRequest request) {
        KioskResponse response = kioskService.createKiosk(request);
        return ResponseEntity.ok(response);
    }

    // 키오스크 수정
    @PutMapping("/{id}")
    @Operation(summary = "키오스크 수정", description = "키오스크 정보를 수정하는 API")
    public ResponseEntity<KioskResponse> updateKiosk(@PathVariable Integer id, @RequestBody KioskRequest request) {
        KioskResponse response = kioskService.updateKiosk(id, request);
        return ResponseEntity.ok(response);
    }

    // 키오스크 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "키오스크 삭제", description = "키오스크 정보를 삭제하는 API")
    public ResponseEntity<Void> deleteKiosk(@PathVariable Integer id) {
        kioskService.deleteKiosk(id);
        return ResponseEntity.noContent().build();
    }

    // 모든 키오스크 조회
    @GetMapping
    @Operation(summary = "키오스크 전체 조회", description = "키오스크 정보를 전체 조회하는 API")
    public ResponseEntity<List<KioskResponse>> getAllKiosks() {
        List<KioskResponse> kiosks = kioskService.getAllKiosks();
        return ResponseEntity.ok(kiosks);
    }
}
