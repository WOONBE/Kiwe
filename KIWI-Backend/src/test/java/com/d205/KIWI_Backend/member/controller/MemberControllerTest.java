package com.d205.KIWI_Backend.member.controller;

import com.d205.KIWI_Backend.KiwiBackendApplication;
import com.d205.KIWI_Backend.member.dto.MemberRequest;
import com.d205.KIWI_Backend.member.dto.MemberResponse;
import com.d205.KIWI_Backend.member.dto.SignInRequest;
import com.d205.KIWI_Backend.member.dto.SignInResponse;
import com.d205.KIWI_Backend.member.repository.MemberRepository;
import com.d205.KIWI_Backend.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {

      @Mock
      private MemberService memberService;

      @InjectMocks
      private MemberController memberController;

      @BeforeEach
      void setUp() {
          MockitoAnnotations.openMocks(this);
      }

    @Test
    @DisplayName("회원 가입 테스트")
    void registerMemberTest() throws Exception {

        //given
        MemberRequest memberRequest = new MemberRequest("testName", "testEmail@example.com", "password123", null);
        MemberResponse memberResponse = new MemberResponse(1, "testName", "testEmail@example.com", Collections.emptyList(), null);


        //when
        when(memberService.registerMember(any(MemberRequest.class))).thenReturn(memberResponse);

        //then
        ResponseEntity<MemberResponse> result = memberController.registerMember(memberRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(memberResponse, result.getBody());


    }

    @Test
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {

        //given
        SignInRequest signInRequest = new SignInRequest("testEmail@example.com", "password123");
        SignInResponse signInResponse = new SignInResponse("testEmail@example.com", null, "accessToken", "refreshToken");

        //when
        Mockito.when(memberService.signIn(signInRequest.getEmail(), signInRequest.getPassword())).thenReturn(signInResponse);

        //then
        ResponseEntity<SignInResponse> result = memberController.signIn(signInRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(signInResponse, result.getBody());

    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void getAllUsersTest() throws Exception {

        //given
        MemberResponse memberResponse = new MemberResponse(1, "testName", "testEmail@example.com", Collections.emptyList(), null);
        List<MemberResponse> memberList = List.of(memberResponse);

        //when
        when(memberService.getAllUsers()).thenReturn(memberList);

        //then
        ResponseEntity<List<MemberResponse>> result = memberController.getAllUsers();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(memberList, result.getBody());
    }

    @Test
    @DisplayName("ID로 사용자 조회 테스트")
    void findMemberByIdTest() throws Exception {

        //given
        int memberId = 1;
        MemberResponse memberResponse = new MemberResponse(memberId, "testName", "testEmail@example.com", Collections.emptyList(), null);

        //when
        when(memberService.findMemberById(eq(memberId))).thenReturn(memberResponse);

        //then
        ResponseEntity<MemberResponse> result = memberController.findMemberById(memberId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(memberResponse, result.getBody());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 테스트")
    void findMemberByEmailTest() throws Exception {
        String email = "testEmail@example.com";
        MemberResponse memberResponse = new MemberResponse(1, "testName", email, Collections.emptyList(), null);

        when(memberService.findMemberByEmail(eq(email))).thenReturn(memberResponse);


        ResponseEntity<MemberResponse> result = memberController.findMemberByEmail(email);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(memberResponse, result.getBody());


    }
}
