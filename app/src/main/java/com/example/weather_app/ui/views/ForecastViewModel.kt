package com.example.weather_app.ui.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.R
import com.example.weather_app.data.ForecastRepository
import com.example.weather_app.data.models.CitiesResponse
import com.example.weather_app.data.models.City
import com.example.weather_app.data.models.Data
import com.example.weather_app.data.models.ForecastResponse
import com.example.weather_app.network.NetworkState
import com.example.weather_app.utils.AppUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val repository: ForecastRepository
) : ViewModel() {

    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Int>()
    val noRemoteDataLiveData = MutableLiveData<Boolean>()
    val dataNotExistLiveData = MutableLiveData<Boolean>()

    val citiesLiveData = MutableLiveData<MutableList<City>>()
    val forecastLiveData = MutableLiveData<List<Data>>()

    fun getCities() {
        viewModelScope.launch {
            repository.getCitiesData {
                when (it.status) {
                    NetworkState.Status.RUNNING -> {
                        loadingLiveData.value = true
                    }
                    NetworkState.Status.SUCCESS -> {
                        loadingLiveData.value = false
                        citiesLiveData.value = (it.data as CitiesResponse).cities
                    }
                    NetworkState.Status.FAILED -> {
                        loadingLiveData.value = false
                        errorLiveData.value = AppUtils.getErrorMessage(it.msg as Throwable)
                    }
                }
            }
        }
    }

    fun getCityForecast(cityID: Int, lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getForecastOfCity(lat, lon) {
                when (it.status) {
                    NetworkState.Status.RUNNING -> {
                        loadingLiveData.value = true
                    }
                    NetworkState.Status.SUCCESS -> {
                        saveCityForecast(cityID, it.data as ForecastResponse)
                        loadingLiveData.value = false
                        dataNotExistLiveData.value = false
                        forecastLiveData.value = it.data.list
                    }
                    NetworkState.Status.FAILED -> {
                        noRemoteDataLiveData.value = true
                    }
                }
            }
        }
    }

    fun getLocalData(cityID: Int) {
        viewModelScope.launch {
            repository.getSavedForecast(cityID.toString()).collect {
                val type = object : TypeToken<ForecastResponse>() {}.type
                val response = Gson().fromJson<ForecastResponse>(it, type)

                loadingLiveData.value = false

                if (response != null) {
                    errorLiveData.value = R.string.not_accurate_data
                    forecastLiveData.value = response.list
                    dataNotExistLiveData.value = false
                } else {
                    dataNotExistLiveData.value = true
                }
                this.cancel()
            }
        }
    }

    private fun saveCityForecast(cityID: Int, data: ForecastResponse) {
        val dataString = Gson().toJson(data)
        viewModelScope.launch {
            repository.saveCityForecast(cityID.toString(), dataString)
        }
    }

}