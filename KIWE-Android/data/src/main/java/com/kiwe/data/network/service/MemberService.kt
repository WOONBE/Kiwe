package com.kiwe.data.network.service

import com.kiwe.data.network.util.getResult
import com.kiwe.domain.model.MemberInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import javax.inject.Inject

class MemberService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun searchMemberByEmail(email: String): Result<MemberInfoResponse> =
            client.getResult("api/members/email") {
                parameter("email", email)
            }

        suspend fun searchMemberById(id: Int): Result<MemberInfoResponse> = client.getResult("api/members/$id")
    }
