package com.d205.KIWI_Backend.order.service;


import com.d205.KIWI_Backend.order.domain.ChooseOption;
import com.d205.KIWI_Backend.order.repository.ChooseOptionRepository;
import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionCSVService {

    private final ChooseOptionRepository chooseOptionRepository;

    public void readCSVAndSaveData(String filename) {
        List<ChooseOption> chooseOptions = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String[] nextLine;
            csvReader.readNext(); // 헤더 스킵
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    ChooseOption chooseOption = ChooseOption.builder()
                        .engName(nextLine[1]) // 영어 이름
                        .korName(nextLine[2]) // 한글 이름
                        .normalDrinkPrice(Integer.parseInt(nextLine[3])) // 일반 음료 가격
                        .megaDrinkPrice(Integer.parseInt(nextLine[4])) // 메가 음료 가격
                        .build();

                    chooseOptions.add(chooseOption);
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식이 있습니다: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("CSV 형식이 잘못되었습니다: " + e.getMessage() + " at line: " + String.join(",", nextLine));
                }
            }
            chooseOptionRepository.saveAll(chooseOptions); // 모든 ChooseOption 엔티티를 저장
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류: " + e.getMessage());
        }
    }
}
