package com.example.weatherapp.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.logic.entity.NowTemp
import com.example.weatherapp.logic.model.NowData

@Dao
interface NowTempDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNowTemp(nowTemp: NowTemp)

    @Query("SELECT obsTime,`temp`,feelsLike, icon, text, wind360, windDir, windScale, windSpeed, humidity, precip, pressure, vis, cloud, dew FROM `now-temp` WHERE id = :id")
    suspend fun getNowTempByIdWithoutId(id:String): NowData?

    @Query("SELECT * FROM `now-temp` WHERE id = :id")
    suspend fun getNowTempById(id:String): NowTemp?

}