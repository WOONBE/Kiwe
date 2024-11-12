package com.kiwe.domain.usecase.manager.menu

interface DeleteMenuUseCase {
    suspend operator fun invoke(menuId: Int): Result<Unit>
}