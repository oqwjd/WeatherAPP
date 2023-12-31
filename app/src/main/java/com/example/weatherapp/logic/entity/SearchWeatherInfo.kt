package com.example.weatherapp.logic.entity

import androidx.room.Entity

@Entity(tableName = "search-weather-info", primaryKeys = ["location"])
data class SearchWeatherInfo(val location:String,
                             val updateTime:String,
                             val searchNetTime:String
)
