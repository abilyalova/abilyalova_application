package com.delybills.makeaway.data.network

import android.content.Context
import com.delybills.makeaway.BuildConfig
import com.delybills.makeaway.common.UserPrefsManager
import com.delybills.makeaway.data.network.api.TokensApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://makeaway-lyceum.ru/"
    }

    fun <Api> provideApi(
        api: Class<Api>,
        context: Context,
    ): Api {
        val authenticator =
            JWTAuthenticator(context, provideTokensApi())
        val accessToken = UserPrefsManager(context).accessToken
        val refreshToken = UserPrefsManager(context).refreshToken

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHTPPClient(authenticator, accessToken, refreshToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun provideTokensApi(): TokensApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHTPPClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokensApi::class.java)
    }

    private fun provideOkHTPPClient(
        authenticator: JWTAuthenticator? = null,
        accessToken: String? = null,
        refreshToken: String? = null
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                        if (accessToken != null) {
                            it.addHeader(
                                "Authorization",
                                "Bearer $accessToken"
                            )
                        }
                        if (refreshToken != null) {
                            it.addHeader(
                                "x-refresh-token",
                                "$refreshToken"
                            )
                        }
                    }.build()
                )
            }
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
                if (authenticator != null) {
                    client.authenticator(authenticator)
                }
            }.build()
    }
}