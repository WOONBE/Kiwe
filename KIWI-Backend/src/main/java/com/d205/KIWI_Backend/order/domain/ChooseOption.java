package com.d205.KIWI_Backend.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ChooseOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String engName;
    private String korName;
    private int normalDrinkPrice;
    private int megaDrinkPrice;

    @Builder
    public ChooseOption(String engName, String korName, int normalDrinkPrice, int megaDrinkPrice) {
        this.engName = engName;
        this.korName = korName;
        this.normalDrinkPrice = normalDrinkPrice;
        this.megaDrinkPrice = megaDrinkPrice;
    }
}
