package com.example.weather_app.network

import com.example.weather_app.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val originalUrl = originalRequest.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("appid", Constants.API_KEY)
            .addQueryParameter("units", "metric")
            .build()

        val requestBuilder = originalRequest.newBuilder()
            .url(url)
            .addHeader("Content-Type", "application/json")

        val request = requestBuilder.build()

        val response = chain.proceed(request)

        response.code//status code
        return response

    }
}
