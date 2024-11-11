package com.kiwe.data.usecase.manager.edit

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.edit.EditMyInfoUseCase
import javax.inject.Inject

class EditMyInfoUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : EditMyInfoUseCase {
        override suspend operator fun invoke(editMemberParam: EditMemberParam): Result<MemberInfoResponse> =
            memberService.editMyInfo(editMemberParam)
    }
