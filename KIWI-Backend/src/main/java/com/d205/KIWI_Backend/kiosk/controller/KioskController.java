package com.d205.KIWI_Backend.kiosk.controller;

import com.d205.KIWI_Backend.kiosk.dto.KioskOrderNumberResponse;
import com.d205.KIWI_Backend.kiosk.dto.KioskRequest;
import com.d205.KIWI_Backend.kiosk.dto.KioskResponse;
import com.d205.KIWI_Backend.kiosk.dto.KioskSignOutRequest;
import com.d205.KIWI_Backend.kiosk.service.KioskService;
import com.d205.KIWI_Backend.member.service.BlackListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kiosks")
@RequiredArgsConstructor
public class KioskController {

    private final KioskService kioskService;
    private final BlackListService blackListService;

    // 키오스크 생성
    @PostMapping
    @Operation(summary = "키오스크 생성", description = "키오스크를 생성하는 API")
    public ResponseEntity<KioskResponse> createKiosk(@RequestBody KioskRequest request) {
        KioskResponse response = kioskService.createKiosk(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{kioskId}")
    @Operation(summary = "키오스크 조회", description = "키오스크 ID로 단일 키오스크 정보를 조회하는 API")
    public ResponseEntity<KioskResponse> getKioskById(@PathVariable Integer kioskId) {
        KioskResponse kioskResponse = kioskService.getKioskById(kioskId);
        return ResponseEntity.ok(kioskResponse);
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
    @GetMapping("/all")
    @Operation(summary = "키오스크 전체 조회", description = "키오스크 정보를 전체 조회하는 API")
    public ResponseEntity<List<KioskResponse>> getAllKiosks() {
        List<KioskResponse> kiosks = kioskService.getAllKiosks();
        return ResponseEntity.ok(kiosks);
    }

//    @PostMapping("/log-out")
//    @Operation(summary = "키오스크 로그아웃", description = "비밀번호를 사용하여 키오스크 로그아웃을 수행합니다.")
//    public ResponseEntity<Void> kioskSignOut(@RequestHeader("Refresh-Token") String refreshToken,
//        @RequestParam String password) {
//        try {
//            blackListService.kioskSignOut(password, refreshToken); // 비밀번호와 Refresh Token으로 로그아웃 수행
//            return ResponseEntity.noContent().build(); // 성공적으로 로그아웃된 경우 204 No Content 반환
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().build(); // 비밀번호 불일치 또는 기타 예외 처리
//        }
//    }

    @PostMapping("/log-out")
    @Operation(summary = "키오스크 로그아웃", description = "비밀번호를 사용하여 키오스크 로그아웃을 수행합니다.")
    public ResponseEntity<Void> kioskSignOut(@RequestBody KioskSignOutRequest request) {
        try {
            blackListService.kioskSignOut(request.getPassword(), request.getRefreshToken()); // 비밀번호와 Refresh Token으로 로그아웃 수행
            return ResponseEntity.noContent().build(); // 성공적으로 로그아웃된 경우 204 No Content 반환
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build(); // 비밀번호 불일치 또는 기타 예외 처리
        }
    }

    @GetMapping
    @Operation(summary = "나의 키오스크 목록 조회", description = "로그인한 사용자의 키오스크 목록을 조회하는 API")
    public ResponseEntity<List<KioskResponse>> getMyKiosks() {
        List<KioskResponse> myKiosks = kioskService.getMyKiosks();
        return ResponseEntity.ok(myKiosks);
    }

    @PutMapping("/my/{kioskId}")
    @Operation(summary = "나의 키오스크 정보 수정", description = "로그인한 사용자가 자신의 키오스크 정보를 수정하는 API")
    public ResponseEntity<KioskResponse> updateMyKiosk(
        @PathVariable Integer kioskId,
        @RequestBody KioskRequest kioskRequest) {

        KioskResponse updatedKiosk = kioskService.updateMyKiosk(kioskId, kioskRequest);
        return ResponseEntity.ok(updatedKiosk);
    }

    @GetMapping("/kiosk-order")
    @Operation(summary = "키오스크 순서 조회", description = "키오스크ID를 통해 owner의 몇 번째 키오스크인지 반환")
    public ResponseEntity<KioskOrderNumberResponse> getKioskOrderNumber(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("kioskId") Long kioskId) {
        return ResponseEntity.ok(kioskService.getKioskOrderNumber(ownerId, kioskId));
    }

}
