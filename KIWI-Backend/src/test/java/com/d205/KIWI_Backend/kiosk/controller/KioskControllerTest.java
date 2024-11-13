package com.d205.KIWI_Backend.kiosk.controller;

import com.d205.KIWI_Backend.kiosk.dto.KioskRequest;
import com.d205.KIWI_Backend.kiosk.dto.KioskResponse;
import com.d205.KIWI_Backend.kiosk.dto.KioskOrderNumberResponse;
import com.d205.KIWI_Backend.kiosk.service.KioskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class KioskControllerTest {

    @Mock
    private KioskService kioskService;

    @InjectMocks
    private KioskController kioskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("키오스크 생성 테스트")
    void createKiosk() throws Exception {

        //given
        KioskRequest request = new KioskRequest("Seoul", "ACTIVE", 1);
        KioskResponse response = new KioskResponse(1, "Seoul", "ACTIVE");

        //when
        when(kioskService.createKiosk(any(KioskRequest.class))).thenReturn(response);

        //then
        ResponseEntity<KioskResponse> result = kioskController.createKiosk(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(response.getId(), result.getBody().getId());

        verify(kioskService, times(1)).createKiosk(any(KioskRequest.class));
    }

    @Test
    @DisplayName("키오스크 단건 조회 테스트")
    void getKioskById() throws Exception {
        //given
        KioskRequest request = new KioskRequest("Seoul", "ACTIVE", 1);
        KioskResponse response = new KioskResponse(1, "Seoul", "ACTIVE");

        //when
        when(kioskService.getKioskById(1)).thenReturn(response);

        //then
        ResponseEntity<KioskResponse> result = kioskController.getKioskById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(response.getId(), result.getBody().getId());


        verify(kioskService, times(1)).getKioskById(1);
    }

    @Test
    @DisplayName("키오스크 업데이트 테스트")
    void updateKiosk() throws Exception {

        KioskRequest request = new KioskRequest("Busan", "INACTIVE", 1);
        KioskResponse response = new KioskResponse(1, "Busan", "INACTIVE");

        when(kioskService.updateKiosk(eq(1), any(KioskRequest.class))).thenReturn(response);

        ResponseEntity<KioskResponse> result = kioskController.updateKiosk(1, request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(response.getId(), result.getBody().getId());


        verify(kioskService, times(1)).updateKiosk(eq(1), any(KioskRequest.class));
    }


    @Test
    @DisplayName("모든 키오스크 조회 테스트")
    void getAllKiosks() throws Exception {
        KioskResponse response1 = new KioskResponse(1, "Seoul", "ACTIVE");
        KioskResponse response2 = new KioskResponse(2, "Busan", "INACTIVE");
        List<KioskResponse> kiosks = List.of(response1, response2);

        when(kioskService.getAllKiosks()).thenReturn(kiosks);
        ResponseEntity<List<KioskResponse>> result = kioskController.getAllKiosks();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(response1.getId(), result.getBody().get(0).getId());
        assertEquals(response2.getId(), result.getBody().get(1).getId());
        

        verify(kioskService, times(1)).getAllKiosks();
    }

//    @Test
//    void getMyKiosks() throws Exception {
//        KioskResponse response1 = new KioskResponse(1, "Seoul", "ACTIVE");
//        KioskResponse response2 = new KioskResponse(2, "Busan", "INACTIVE");
//        List<KioskResponse> kiosks = List.of(response1, response2);
//
//        when(kioskService.getMyKiosks()).thenReturn(kiosks);
//
//        mockMvc.perform(get("/api/kiosks"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].id").value(response1.getId()))
//            .andExpect(jsonPath("$[1].id").value(response2.getId()));
//
//        verify(kioskService, times(1)).getMyKiosks();
//    }
//
//    @Test
//    void getKioskOrderNumber() throws Exception {
//        KioskOrderNumberResponse response = new KioskOrderNumberResponse(1L, 5L);
//
//        when(kioskService.getKioskOrderNumber(1L, 1L)).thenReturn(response);
//
//        mockMvc.perform(get("/api/kiosks/kiosk-order")
//                .param("ownerId", "1")
//                .param("kioskId", "1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.kioskId").value(response.getKioskId()))
//            .andExpect(jsonPath("$.kioskOrderNumber").value(response.getKioskOrderNumber()));
//
//        verify(kioskService, times(1)).getKioskOrderNumber(1L, 1L);
//    }
}
