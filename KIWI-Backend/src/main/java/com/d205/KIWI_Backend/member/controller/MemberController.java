package com.d205.KIWI_Backend.member.controller;

import com.d205.KIWI_Backend.member.dto.MemberRequest;
import com.d205.KIWI_Backend.member.dto.MemberResponse;
import com.d205.KIWI_Backend.member.dto.SignInResponse;
import com.d205.KIWI_Backend.member.service.BlackListService;
import com.d205.KIWI_Backend.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BlackListService blackListService;

    // 회원 등록
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원 가입을 진행하는 API")
    public ResponseEntity<MemberResponse> registerMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.registerMember(memberRequest);
        return ResponseEntity.ok(memberResponse);
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 진행하는 API")
    public ResponseEntity<SignInResponse> signIn(@RequestParam String email, @RequestParam String password) {
        SignInResponse signInResponse = memberService.signIn(email, password);
        return ResponseEntity.ok(signInResponse);
    }

    // OAuth 로그인
    @PostMapping("/oauth-login")
    @Operation(summary = "구글 로그인", description = "구글 로그인 및 계정 등록을 진행하는 API")
    public ResponseEntity<SignInResponse> oauthSignIn(@RequestParam String email) {
        SignInResponse signInResponse = memberService.oauthSignIn(email);
        return ResponseEntity.ok(signInResponse);
    }

    // 회원 정보 업데이트
    @PutMapping("/{memberId}")
    @Operation(summary = "멤버 수정", description = "멤버 정보를 수정하는 API")
    public ResponseEntity<MemberResponse> updateMemberInfo(
            @PathVariable Integer memberId,
            @RequestBody MemberRequest memberRequest
    ) {
        MemberResponse updatedMember = memberService.updateMemberInfo(memberId, memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // 모든 사용자 조회
    @GetMapping("/all")
    @Operation(summary = "모든 멤버 조회", description = "모든 멤버를 조회하는 API")
    public ResponseEntity<List<MemberResponse>> getAllUsers() {
        List<MemberResponse> users = memberService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ID로 사용자 조회
    @GetMapping("/{memberId}")
    @Operation(summary = "memberId로 조회", description = "memberId로 조회하는 API")
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable Integer memberId) {
        MemberResponse memberResponse = memberService.findMemberById(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    // 이메일로 사용자 조회
    @GetMapping("/email")
    @Operation(summary = "이메일 조회", description = "이메일로 사용자 정보를 조회하는 API")
    public ResponseEntity<MemberResponse> findMemberByEmail(@RequestParam String email) {
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/log-out")
    @Operation(summary = "로그아웃", description = "로그인 된 사용자의 로그 아웃을 진행하는 API, Refresh Token을 BlackList 처리 + Access Token은 Redis에서 삭제")
    public ResponseEntity<Void> signOut(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String accessToken = (String) authentication.getCredentials();
            System.out.println("Refresh Token: " + accessToken);
            blackListService.signOut(accessToken,refreshToken);
            return ResponseEntity.noContent().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
