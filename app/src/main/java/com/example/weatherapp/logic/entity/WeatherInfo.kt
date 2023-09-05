package com.example.weatherapp.logic.entity

import androidx.room.Entity

@Entity(tableName = "weather-info", primaryKeys = ["id","day"])
data class WeatherInfo(val id:String,
                       val day:Int,
                       val fxDate:String,
                       val sunrise:String,
                       val sunset:String,
                       val moonrise:String,
                       val moonset:String,
                       val moonPhase:String,
                       val moonPhaseIcon:String,
                       val tempMax:String,
                       val tempMin:String,
                       val iconDay:String,
                       val textDay:String,
                       val iconNight:String,
                       val textNight:String,
                       val wind360Day:String,
                       val windDirDay:String,
                       val windScaleDay:String,
                       val windSpeedDay:String,
                       val wind360Night:String,
                       val windDirNight:String,
                       val windScaleNight:String,
                       val windSpeedNight:String,
                       val humidity:String,
                       val precip:String,
                       val pressure:String,
                       val vis:String,
                       val cloud:String,
                       val uvIndex:String
)
