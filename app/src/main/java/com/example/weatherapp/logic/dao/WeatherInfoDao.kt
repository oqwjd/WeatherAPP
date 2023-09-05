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

    @Query("SELECT * FROM 'weather-info' WHERE id = :id")
    suspend fun getWeatherInfoById(id: String): ArrayList<Daily>?


    @Query("DELETE FROM 'weather-info' WHERE id = :id")
    suspend fun deleteAllWeatherInfoById(id:String)
}