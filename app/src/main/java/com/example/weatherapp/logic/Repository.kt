package com.example.weatherapp.logic

import android.content.Context
import androidx.lifecycle.liveData
import com.example.weatherapp.logic.entity.SearchWeatherInfo
import com.example.weatherapp.logic.model.Daily
import com.example.weatherapp.logic.model.DailyDate
import com.example.weatherapp.logic.model.Weather
import com.example.weatherapp.logic.model.toLifeIndex
import com.example.weatherapp.logic.model.toWeatherInfo
import com.example.weatherapp.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

object Repository {

    private lateinit var dataBaseInstance: Database
    fun initialize(context: Context) {
        dataBaseInstance = Database.getInstance(context.applicationContext)
    }
    fun searchPlaces(query:String) = fire(Dispatchers.IO){
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.code == "200"){
            val places = placeResponse.location
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.code}"))
        }
    }

    fun searchWeather(query: String) = fire(Dispatchers.IO) {
        coroutineScope {
            var ifShouldCheckNet = true
            val nowTime = ZonedDateTime.now()
            val shouldCheckNet = async {
                val searchWeatherInfoDao = Repository.dataBaseInstance.searchWeatherInfoDao()
                val searchWeatherInfoData = searchWeatherInfoDao.getWeatherByLocation(query)
                if (searchWeatherInfoData!=null){
                    if(Duration.between(ZonedDateTime.parse(
                            searchWeatherInfoData.updateTime,
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME),nowTime).seconds<300
                        && Duration.between(ZonedDateTime.parse(
                            searchWeatherInfoData.searchNetTime,
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME),nowTime).seconds<300)
                        ifShouldCheckNet = false
                }
                ifShouldCheckNet = true
            }
            shouldCheckNet.await()
            if (ifShouldCheckNet){
                val deferredReadLifeIndex = async {
                    SunnyWeatherNetwork.searchLifeIndex(query)
                }
                val deferredReadWeatherInfo = async {
                    SunnyWeatherNetwork.searchWeatherInfo(query)
                }
                val lifeIndexResponse = deferredReadLifeIndex.await()
                val weatherResponse = deferredReadWeatherInfo.await()
                if (lifeIndexResponse.code == "200" && weatherResponse.code == "200") {
                    async(Dispatchers.IO) {
                        for (lifeIndexData in lifeIndexResponse.data){
                            Repository.dataBaseInstance.lifeIndexDao().insertLifeIndex(lifeIndexData.toLifeIndex(query))
                        }
                        var day = 1
                        for (weatherData in weatherResponse.data){
                            Repository.dataBaseInstance.weatherInfoDao().insertWeatherInfo(weatherData.toWeatherInfo(query,day++))
                        }
                        Repository.dataBaseInstance.searchWeatherInfoDao().insertWeather(
                            SearchWeatherInfo(query,lifeIndexResponse.updateTime,nowTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        )
                    }.await()
                    val weathers = Weather(lifeIndexResponse.data,weatherResponse.data)
                    Result.success(weathers)
                } else {
                    Result.failure(RuntimeException("response status :lifeIndex-${lifeIndexResponse.code},weatherInfo-${weatherResponse.code}"))
                }
            }else{
                var weatherInfo: ArrayList<Daily>? = null
                var lifeIndex: ArrayList<DailyDate>? = null
                async(Dispatchers.Default) {
                    weatherInfo = Repository.dataBaseInstance.weatherInfoDao().getWeatherInfoById(query)
                }.await()

                async(Dispatchers.Default) {
                    lifeIndex = Repository.dataBaseInstance.lifeIndexDao().getLifeIndexById(query)
                }.await()
                Result.success(Weather(lifeIndex!!,weatherInfo!!))
            }
        }
    }

    private fun <T> fire(context:CoroutineContext,block:suspend ()->Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            }catch (e:Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }


}
