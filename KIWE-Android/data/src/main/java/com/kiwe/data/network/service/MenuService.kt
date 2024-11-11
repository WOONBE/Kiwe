package com.kiwe.data.network.service

import com.kiwe.data.network.util.getResult
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.CreateMenuParam
import com.kiwe.domain.model.MenuCategoryParam
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class MenuService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun getCategoryList(category: String): Result<List<MenuCategoryParam>> =
            client
                .getResult<List<MenuCategoryParam>>("api/menus/category") {
                    parameter("category", category)
                    contentType(ContentType.Application.Json)
                }

        suspend fun getMenuById(menuId: Int): Result<MenuCategoryParam> = client.getResult("api/menus/$menuId")

        suspend fun getAllMenuList(): Result<List<MenuCategoryParam>> = client.getResult("api/menus/all")

        suspend fun createMenu(createMenuParam: CreateMenuParam): Result<MenuCategoryParam> =
            client.postResult("api/menus") {
                setBody(createMenuParam)
            }
    }
