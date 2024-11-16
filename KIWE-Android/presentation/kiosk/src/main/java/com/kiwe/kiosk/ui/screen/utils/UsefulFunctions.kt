package com.kiwe.kiosk.ui.screen.utils

import com.kiwe.kiosk.BuildConfig.BASE_IMAGE_URL

fun String.prefixingImagePaths() = "https://$BASE_IMAGE_URL$this"
