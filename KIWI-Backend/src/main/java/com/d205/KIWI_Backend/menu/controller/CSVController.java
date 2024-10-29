package com.d205.KIWI_Backend.menu.controller;

import com.d205.KIWI_Backend.menu.service.CSVService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CSVController {

    private final CSVService csvService;

    @GetMapping("/import-csv/{filename}")
    @Operation(summary = "메뉴 csv로 입력", description = "메뉴 csv로 입력하는 API")
    public ResponseEntity<String> importCSV(@PathVariable String filename) {
        Resource resource = new ClassPathResource(filename);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("파일이 존재하지 않습니다: " + filename);
        }

        try {
            csvService.readCSVAndSaveData(filename); // 메뉴 CSV 데이터 가져오기
            return new ResponseEntity<>("Successfully imported records from " + filename, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error importing CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
