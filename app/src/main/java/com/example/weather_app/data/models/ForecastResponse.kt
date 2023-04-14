package com.example.weather_app.data.models

data class ForecastResponse(
    val cnt: Int,
    val cod: String,
    val list: List<Data>,
    val message: Int
)