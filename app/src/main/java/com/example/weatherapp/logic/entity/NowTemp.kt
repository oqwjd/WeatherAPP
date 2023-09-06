package com.example.weatherapp.logic.entity

import androidx.room.Entity
import com.example.weatherapp.logic.model.NowData

@Entity(tableName = "now-temp", primaryKeys = ["id"])
data class NowTemp(val id:String,
                   val searchNetTime:String,
                   val obsTime:String,
                   val temp:String,
                   val feelsLike:String,
                   val icon: String,
                   val text: String,
                   val wind360: String,
                   val windDir: String,
                   val windScale: String,
                   val windSpeed: String,
                   val humidity: String,
                   val precip: String,
                   val pressure: String,
                   val vis: String,
                   val cloud: String,
                   val dew: String
                   )

fun NowTemp.toNowData():NowData{
    return NowData(obsTime, temp, feelsLike, icon, text, wind360, windDir, windScale, windSpeed, humidity, precip, pressure, vis, cloud, dew)
}
