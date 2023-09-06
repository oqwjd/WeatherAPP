package com.example.weatherapp.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.example.weatherapp.logic.dao.NowTempDao
import com.example.weatherapp.logic.entity.NowTemp
import com.example.weatherapp.logic.entity.SearchWeatherInfo
import com.example.weatherapp.logic.entity.toNowData
import com.example.weatherapp.logic.model.Daily
import com.example.weatherapp.logic.model.DailyDate
import com.example.weatherapp.logic.model.NowData
import com.example.weatherapp.logic.model.Weather
import com.example.weatherapp.logic.model.toLifeIndex
import com.example.weatherapp.logic.model.toNowTemp
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

    private lateinit var dataBaseInstance: AppDatabase
    fun initialize(context: Context) {
        dataBaseInstance = AppDatabase.getInstance(context.applicationContext)
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
            var ifShouldCheckTempNow = true
            val nowTime = ZonedDateTime.now()
            var nowTempData : NowTemp? = null

            val shouldCheckTempNow = async {
                Log.d("debug in Repository","in shouldCheckTempNow")
                val nowTempDao = Repository.dataBaseInstance.nowTempDao()
                nowTempData = nowTempDao.getNowTempById(query)
                if (nowTempData!=null){
                    if (Duration.between(ZonedDateTime.parse(nowTempData!!.searchNetTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),nowTime).seconds<600)
                        ifShouldCheckTempNow = false
                }
                Log.d("debug in Repository","out shouldCheckTempNow")
            }

            val shouldCheckNet = async {
                val searchWeatherInfoDao = Repository.dataBaseInstance.searchWeatherInfoDao()
                val searchWeatherInfoData = searchWeatherInfoDao.getWeatherByLocation(query)
                if (searchWeatherInfoData!=null){
                    if(Duration.between(ZonedDateTime.parse(searchWeatherInfoData.searchNetTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),nowTime).seconds<7200)
                        ifShouldCheckNet = false
                }
            }

            shouldCheckTempNow.await()
            Log.d("debug in Repository","ifShouldCheckNetNow(temp now):$ifShouldCheckTempNow")
            if (ifShouldCheckTempNow){
                //实时温度需要刷新，nowTempData覆盖为查询回来的时间
                val deferredReadNowTemp = async{
                    SunnyWeatherNetwork.searchNowTemp(query)
                }
                val nowTempResponse = deferredReadNowTemp.await()
                if (nowTempResponse.code == "200"){
                    nowTempData = nowTempResponse.now.toNowTemp(query,nowTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    async(Dispatchers.IO) {
                        Repository.dataBaseInstance.nowTempDao().insertNowTemp(nowTempData!!                        )
                    }
                }
            }

            shouldCheckNet.await()
            Log.d("debug in Repository","ifShouldCheckNet(LifeIndex and WeatherInfo):$ifShouldCheckNet")
            if (ifShouldCheckNet){
                val deferredReadLifeIndex = async {
                    SunnyWeatherNetwork.searchLifeIndex(query)
                }
                val deferredReadWeatherInfo = async {
                    SunnyWeatherNetwork.searchWeatherInfo(query)
                }
                val lifeIndexResponse = deferredReadLifeIndex.await()
                val weatherResponse = deferredReadWeatherInfo.await()
//                Log.d("debug in Repository","weatherResponse:${weatherResponse.code},lifeIndexResponse:${lifeIndexResponse.code}")
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
                    val weathers = Weather(lifeIndexResponse.data,weatherResponse.data,nowTempData!!.toNowData())
                    Result.success(weathers)
                } else {
                    Result.failure(RuntimeException("response status :lifeIndex-${lifeIndexResponse.code},weatherInfo-${weatherResponse.code}"))
                }
            }else{
                var weatherInfo: ArrayList<Daily>? = null
                var lifeIndex: ArrayList<DailyDate>? = null
                async(Dispatchers.Default) {
                    weatherInfo = Repository.dataBaseInstance.weatherInfoDao().getWeatherInfoById(query) as ArrayList<Daily>
                }.await()

                async(Dispatchers.Default) {
                    lifeIndex = Repository.dataBaseInstance.lifeIndexDao().getLifeIndexById(query)  as ArrayList<DailyDate>
                }.await()
                Result.success(Weather(lifeIndex!!,weatherInfo!!,nowTempData!!.toNowData()))
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
