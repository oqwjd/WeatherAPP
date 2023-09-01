package com.example.weatherapp.logic.network

import com.example.weatherapp.SunnyWeatherApplication
import com.example.weatherapp.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService{
    @GET("https://geoapi.qweather.com/v2/poi/lookup?type=scenic&key=${SunnyWeatherApplication.Token}")
    fun searchPlaces(@Query("location") query:String): Call<PlaceResponse>
}