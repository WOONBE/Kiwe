package com.d205.KIWI_Backend.menu.service;

import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets; // StandardCharsets 추가

@Service
@RequiredArgsConstructor
public class CSVService {

    private final MenuRepository menuRepository; // MenuRepository를 주입받습니다.


    public void readCSVAndSaveData(String filename) {
        List<Menu> menus = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) { // UTF-8 인코딩 사용
            String[] nextLine;
            csvReader.readNext(); // 헤더 스킵
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    // CSV에서 데이터를 Menu 엔티티에 맞게 변환
                    if (nextLine.length < 7) {
                        System.err.println("CSV 형식이 잘못되었습니다: " + String.join(",", nextLine));
                        continue; // 필요한 열 수가 부족하면 다음 레코드로 넘어감
                    }

                    Menu menu = Menu.builder()
                        .category(nextLine[1]) // 분류
                        .categoryNumber(Integer.parseInt(nextLine[2])) // 카테고리번호
                        .hotOrIce(nextLine[3]) // HOT/ICE
                        .name(nextLine[4]) // 이름
                        .price(Integer.parseInt(nextLine[5])) // 가격
                        .description(nextLine[6]) // 설명
                        .imgPath(
                            nextLine.length > 7 ? nextLine[7] : null) // 이미지 경로, 비어있을 경우 null 설정
                        .build();

                    menus.add(menu);
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식이 있습니다: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("CSV 형식이 잘못되었습니다: " + e.getMessage() + " at line: " + String.join(",", nextLine));
                }
            }
            menuRepository.saveAll(menus); // 모든 Menu 엔티티를 저장
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류: " + e.getMessage());
        }

    }
}
