package com.example.weatherapp.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.logic.entity.WeatherInfo
import com.example.weatherapp.logic.model.Daily

@Dao
interface WeatherInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherInfo(weatherInfo: WeatherInfo)

    @Query("SELECT fxDate, sunrise, sunset, moonrise, moonset, moonPhase, moonPhaseIcon, tempMax, tempMin, iconDay, textDay, iconNight, textNight, wind360Day, windDirDay, windScaleDay, windSpeedDay, wind360Night, windDirNight, windScaleNight, windSpeedNight, humidity, precip, pressure, vis, cloud, uvIndex FROM 'weather-info' WHERE id = :id")
    suspend fun getWeatherInfoById(id: String): List<Daily>?


    @Query("DELETE FROM 'weather-info' WHERE id = :id")
    suspend fun deleteAllWeatherInfoById(id:String)
}