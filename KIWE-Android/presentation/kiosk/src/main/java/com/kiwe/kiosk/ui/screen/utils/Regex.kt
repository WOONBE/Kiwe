package com.kiwe.kiosk.ui.screen.utils

val menuItems =
    listOf(
        "아메리카노",
        "카페\\s*라떼",
        "카푸치노",
        "티라미수 케이크",
        "둥글레차",
        "핫초코",
    )

val menuPattern = menuItems.joinToString("|") { "(?:$it)" } // 비포획 그룹으로 각 항목 감싸기
val menuRegex = Regex(menuPattern)