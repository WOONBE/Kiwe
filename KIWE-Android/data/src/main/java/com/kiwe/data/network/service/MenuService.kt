package com.kiwe.data.network.service

import com.kiwe.data.network.util.getResult
import com.kiwe.domain.model.MenuCategory
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class MenuService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun getCategoryList(category: String): Result<List<MenuCategory>> =
            client
                .getResult<List<MenuCategory>>("api/menus/category") {
                    parameter("category", category)
                    contentType(ContentType.Application.Json)
                }
    }
