package com.example.weatherapp.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.weatherapp.logic.model.PlaceResponse
import com.example.weatherapp.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        Log.d("searchPlaces","into liveData")
        val result = try{
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.code == "200"){
                val places = placeResponse.location
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.code}"))
            }
        }catch (e:Exception){
            Result.failure<List<PlaceResponse>>(e)
        }
        emit(result)
    }
}
