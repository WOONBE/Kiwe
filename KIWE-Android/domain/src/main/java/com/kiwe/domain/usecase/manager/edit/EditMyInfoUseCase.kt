package com.kiwe.domain.usecase.manager.edit

import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.MemberInfoResponse

interface EditMyInfoUseCase {
    suspend operator fun invoke(editMemberParam: EditMemberParam): Result<MemberInfoResponse>
}