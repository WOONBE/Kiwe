package com.kiwe.domain.usecase.manager.edit

import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.MemberInfoResponse

interface EditMemberInfoUseCase {
    suspend operator fun invoke(
        memberId: Int,
        newMemberInfo: EditMemberParam,
    ): Result<MemberInfoResponse>
}
