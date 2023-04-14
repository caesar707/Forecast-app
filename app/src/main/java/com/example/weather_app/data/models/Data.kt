package com.example.weather_app.data.models

data class Data(
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)