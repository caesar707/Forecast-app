package com.example.weather_app.network

import com.example.weather_app.data.models.CitiesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface CitiesAPIService {

    @GET("cities.json")
    fun getCitiesAsync(): Deferred<CitiesResponse>

}