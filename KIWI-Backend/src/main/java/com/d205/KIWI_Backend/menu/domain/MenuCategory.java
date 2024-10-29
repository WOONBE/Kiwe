package com.d205.KIWI_Backend.menu.domain;

public enum MenuCategory {
    DECAFFEINATED("디카페인"),
    SMOOTHIES_AND_FRAPPES("스무디&프라페"),
    NEW_PRODUCTS("신상품"),
    ADE("에이드"),
    BEVERAGES("음료"),
    COFFEE_COLD_BREW("커피(콜드브루)"),
    COFFEE_HOT("커피(HOT)"),
    COFFEE_ICE("커피(ICE)"),
    TEA("티"),
    DESSERT("디저트");

    private final String displayName;

    MenuCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static boolean isValidCategory(String category) {
        for (MenuCategory menuCategory : MenuCategory.values()) {
            if (menuCategory.getDisplayName().equals(category)) {
                return true;
            }
        }
        return false;
    }
}
