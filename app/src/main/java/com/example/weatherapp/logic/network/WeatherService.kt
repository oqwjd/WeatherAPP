package com.example.weatherapp.logic.network

import com.example.weatherapp.SunnyWeatherApplication
import com.example.weatherapp.logic.model.LifeIndexResponse
import com.example.weatherapp.logic.model.NowTempResponse
import com.example.weatherapp.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("https://devapi.qweather.com/v7/indices/1d?type=0&key=${SunnyWeatherApplication.Token}")
    fun searchLifeIndex(@Query("location") query:String) : Call<LifeIndexResponse>

    @GET("https://devapi.qweather.com/v7/weather/7d?key=${SunnyWeatherApplication.Token}")
    fun searchWeatherInfo(@Query("location") query:String) : Call<WeatherResponse>

    @GET("https://devapi.qweather.com/v7/weather/now?key=${SunnyWeatherApplication.Token}")
    fun searchTempNow(@Query("location") query:String) : Call<NowTempResponse>
}