package com.example.weather_app.data.data_source

import com.example.weather_app.network.APIService
import com.example.weather_app.network.CitiesAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: APIService,
    private val citiesAPIService: CitiesAPIService
) {

    suspend fun getCitiesData() =
        withContext(Dispatchers.IO) { citiesAPIService.getCitiesAsync() }.await()

    suspend fun getForecastByCityCoordinates(lat: Double, lon: Double) =
        withContext(Dispatchers.IO) {
            apiService.getForecastOfCityAsync(lat, lon)
        }.await()
}