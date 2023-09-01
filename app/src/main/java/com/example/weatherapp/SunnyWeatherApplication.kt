package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        const val Token = "066d62517028429c9782e628f370faba"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}