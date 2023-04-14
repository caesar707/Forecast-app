package com.example.weather_app.network

import com.example.weather_app.data.models.ForecastResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("forecast")
    fun getForecastOfCityAsync(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Deferred<ForecastResponse>

}