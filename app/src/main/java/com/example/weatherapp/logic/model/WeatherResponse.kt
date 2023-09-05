package com.example.weatherapp.logic.model

import com.example.weatherapp.logic.entity.WeatherInfo
import com.google.gson.annotations.SerializedName

data class WeatherResponse(val code:String,val updateTime:String,val fxLink:String,@SerializedName("daily") val data:ArrayList<Daily>)

data class Daily(val fxDate:String,
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

fun Daily.toWeatherInfo(id:String,day:Int): WeatherInfo{
    return WeatherInfo(id,day, fxDate, sunrise, sunset, moonrise, moonset, moonPhase, moonPhaseIcon, tempMax, tempMin, iconDay, textDay, iconNight, textNight, wind360Day, windDirDay, windScaleDay, windSpeedDay, wind360Night, windDirNight, windScaleNight, windSpeedNight, humidity, precip, pressure, vis, cloud, uvIndex)
}