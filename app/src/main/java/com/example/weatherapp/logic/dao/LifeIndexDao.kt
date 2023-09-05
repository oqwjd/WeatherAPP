package com.example.weatherapp.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.logic.entity.LifeIndex
import com.example.weatherapp.logic.model.DailyDate

@Dao
interface LifeIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLifeIndex(lifeIndex: LifeIndex)

    @Query("SELECT * FROM `life-index` WHERE id = :id")
    suspend fun getLifeIndexById(id:String): ArrayList<DailyDate>?

    @Query("DELETE FROM `life-index` WHERE id = :id")
    suspend fun deleteLifeIndexById(id:String)
}