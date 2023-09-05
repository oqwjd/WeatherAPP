package com.example.weatherapp.logic.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object SunnyWeatherNetwork{
    private val placeService = PlaceServiceCreator.create<PlaceService>()

    private val weatherService = WeatherServiceCreator.create<WeatherService>()

    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()
    suspend fun searchLifeIndex(query:String) = weatherService.searchLifeIndex(query).await()
    suspend fun searchWeatherInfo(query:String) = weatherService.searchWeatherInfo(query).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            continuation.resumeWithException(RuntimeException("response body is null"))
                        } else {
                            continuation.resume(body)
                        }
                    } else {
                        continuation.resumeWithException(RuntimeException("response failed"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}