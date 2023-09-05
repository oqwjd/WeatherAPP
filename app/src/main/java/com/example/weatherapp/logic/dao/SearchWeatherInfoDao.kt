package com.example.weatherapp.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.logic.entity.SearchWeatherInfo

@Dao
interface SearchWeatherInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherInfo: SearchWeatherInfo)

    @Query("SELECT * FROM `search-weather-info` WHERE location = :location")
    suspend fun getWeatherByLocation(location:String): SearchWeatherInfo?

    @Query("DELETE FROM `search-weather-info` WHERE location = :location")
    suspend fun deleteWeatherByLocation(location:String)
}