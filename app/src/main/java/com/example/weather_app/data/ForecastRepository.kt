package com.example.weather_app.data

import com.example.weather_app.data.data_source.LocalDataSource
import com.example.weather_app.data.data_source.RemoteDataSource
import com.example.weather_app.network.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun getCitiesData(onNetworkState: (NetworkState) -> Unit) {
        onNetworkState(NetworkState.LOADING)
        try {
            val result = remoteDataSource.getCitiesData()
            onNetworkState(NetworkState.getLoaded(result))
        } catch (t: Throwable) {
            onNetworkState(NetworkState.getErrorMessage(t))
        }
    }

    suspend fun getForecastOfCity(
        lat: Double,
        lon: Double,
        onNetworkState: (NetworkState) -> Unit
    ) {
        onNetworkState(NetworkState.LOADING)
        try {
            val result = remoteDataSource.getForecastByCityCoordinates(lat, lon)
            onNetworkState(NetworkState.getLoaded(result))
        } catch (t: Throwable) {
            onNetworkState(NetworkState.getErrorMessage(t))
        }
    }

    suspend fun saveCityForecast(cityID: String, forecast: String) {
        localDataSource.saveCityForecast(cityID, forecast)
    }

    fun getSavedForecast(cityID: String): Flow<String?> {
        return localDataSource.getCityForecast(cityID)
    }
}