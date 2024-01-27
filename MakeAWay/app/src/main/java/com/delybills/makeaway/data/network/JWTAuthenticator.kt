package com.delybills.makeaway.data.network

import android.content.Context
import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.common.UserPrefsManager
import com.delybills.makeaway.data.network.api.TokensApi
import com.delybills.makeaway.data.network.networkUtils.ApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class JWTAuthenticator(
    context: Context,
    private val tokensApi: TokensApi
) : Authenticator, BaseRepository() {

    private val prefsManager = UserPrefsManager(context)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = prefsManager.refreshToken
            when (val tokensResponse =
                protectedApiCall { tokensApi.refreshAccessToken("Bearer $refreshToken") }
            ) {
                is ApiResponse.OnSuccessResponse -> {
                    prefsManager.accessToken = tokensResponse.value.access_token

                    prefsManager.refreshToken = tokensResponse.value.refresh_token

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokensResponse.value.access_token}")
                        .build()
                }
                else -> null
            }
        }
    }
}