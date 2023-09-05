package com.example.weatherapp.logic

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.logic.dao.LifeIndexDao
import com.example.weatherapp.logic.dao.SearchWeatherInfoDao
import com.example.weatherapp.logic.dao.WeatherInfoDao

abstract class Database:RoomDatabase() {
    abstract fun lifeIndexDao():LifeIndexDao
    abstract fun searchWeatherInfoDao():SearchWeatherInfoDao
    abstract fun weatherInfoDao():WeatherInfoDao

    companion object{
        @Volatile
        private var instance:Database?=null

        fun getInstance(context:Context):Database{
            return instance?: synchronized(this){
                instance?:buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context:Context):Database{
            return Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "weatherAppDatabase"
            ).build()
        }
    }
}