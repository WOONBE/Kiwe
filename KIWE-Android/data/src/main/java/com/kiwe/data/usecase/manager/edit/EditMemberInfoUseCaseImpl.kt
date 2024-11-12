package com.kiwe.data.usecase.manager.edit

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.edit.EditMemberInfoUseCase
import javax.inject.Inject

class EditMemberInfoUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : EditMemberInfoUseCase {
        override suspend fun invoke(
            memberId: Int,
            newMemberInfo: EditMemberParam,
        ): Result<MemberInfoResponse> = memberService.editMemberInfo(memberId, newMemberInfo)
    }
