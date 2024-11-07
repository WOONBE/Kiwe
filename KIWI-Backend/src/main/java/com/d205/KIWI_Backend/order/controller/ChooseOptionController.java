package com.d205.KIWI_Backend.order.controller;


import com.d205.KIWI_Backend.order.domain.ChooseOption;
import com.d205.KIWI_Backend.order.repository.ChooseOptionRepository;
import com.d205.KIWI_Backend.order.service.OptionCSVService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChooseOptionController {

    private final OptionCSVService csvService;
    private final ChooseOptionRepository chooseOptionRepository;

    @PostMapping("/upload-csv")
    public String uploadCSV(@RequestParam String filename) {
        csvService.readCSVAndSaveData(filename);
        return "CSV data has been uploaded successfully.";
    }

    @GetMapping("/choose-options")
    public List<ChooseOption> getAllChooseOptions() {
        return chooseOptionRepository.findAll();
    }
}
