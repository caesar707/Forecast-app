package com.example.weather_app.data.data_source

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(@ApplicationContext val appContext: Context) {

    private val loggedScope = CoroutineScope(Dispatchers.Default)

    private val Context.dataStore by preferencesDataStore(
        name = "forecastCities",
        scope = loggedScope
    )

    fun getCityForecast(cityID: String): Flow<String?> {
        return appContext.dataStore.data.map {
            it[stringPreferencesKey(cityID)]
        }
    }

    suspend fun saveCityForecast(cityID: String, forecastData: String) {
        appContext.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(cityID)] = forecastData
        }
    }

}