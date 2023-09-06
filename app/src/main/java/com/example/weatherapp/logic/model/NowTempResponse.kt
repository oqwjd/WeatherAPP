package com.example.weatherapp.logic.model

import com.example.weatherapp.logic.entity.NowTemp

data class NowTempResponse(val code:String,val updateTime:String,val fxLink:String,val now: NowData)

data class NowData(val obsTime:String,
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
                   val dew: String)

fun NowData.toNowTemp(id:String, searchNetTime:String):NowTemp{
    return NowTemp(id,searchNetTime, obsTime, temp, feelsLike, icon, text, wind360, windDir, windScale, windSpeed, humidity, precip, pressure, vis, cloud, dew)
}
