package com.example.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.weatherapp.logic.Repository
import com.example.weatherapp.logic.model.Weather

class WeatherViewModel:ViewModel() {
    private val  weatherLiveData = MutableLiveData<String>()

    fun searchWeatherInfo(query: String){
        weatherLiveData.value = query
    }

    val weatherInfoList = ArrayList<Weather>()

    val weatherInfoLiveData = weatherLiveData.switchMap { query ->
        Repository.searchWeather(query.substring(0,9))
    }


}