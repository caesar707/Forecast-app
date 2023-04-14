package com.example.weather_app.data.models

data class City(
    val cityNameAr: String,
    val cityNameEn: String,
    val id: Int,
    val lat: Double,
    val lon: Double
) {
    override fun toString(): String {
        return cityNameEn
    }
}