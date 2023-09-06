package com.example.weatherapp.logic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherapp.logic.dao.LifeIndexDao
import com.example.weatherapp.logic.dao.NowTempDao
import com.example.weatherapp.logic.dao.SearchWeatherInfoDao
import com.example.weatherapp.logic.dao.WeatherInfoDao
import com.example.weatherapp.logic.entity.LifeIndex
import com.example.weatherapp.logic.entity.NowTemp
import com.example.weatherapp.logic.entity.SearchWeatherInfo
import com.example.weatherapp.logic.entity.WeatherInfo

@Database(entities = [LifeIndex::class,SearchWeatherInfo::class,WeatherInfo::class,NowTemp::class], version = 2,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun lifeIndexDao():LifeIndexDao
    abstract fun searchWeatherInfoDao():SearchWeatherInfoDao
    abstract fun weatherInfoDao():WeatherInfoDao
    abstract fun nowTempDao():NowTempDao

    companion object{
        @Volatile
        private var instance:AppDatabase?=null

        fun getInstance(context:Context):AppDatabase{
            return instance?: synchronized(this){
                instance?:buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context:Context):AppDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weatherAppDatabase")
                .build()
        }

//        private val MIGRATION_1_2 = object :Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS 'now-temp'(" +
//                        "'id' TEXT PRIMARY KEY NOT NULL," +
//                        "'searchNetTime' TEXT," +
//                        "'obsTime' TEXT," +
//                        "'temp' TEXT," +
//                        "'feelsLike' TEXT," +
//                        "'icon' TEXT," +
//                        "'text' TEXT," +
//                        "'wind360' TEXT," +
//                        "'windDir' TEXT," +
//                        "'windScale' TEXT," +
//                        "'windSpeed' TEXT," +
//                        "'humidity' TEXT," +
//                        "'precip' TEXT," +
//                        "'pressure' TEXT," +
//                        "'vis' TEXT," +
//                        "'cloud' TEXT," +
//                        "'dew' TEXT" +
//                        ")"
//                )
//            }
//        }
    }
}