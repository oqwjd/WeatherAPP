package com.example.weatherapp.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.databinding.ForecastItemBinding

class WeatherActivity : AppCompatActivity() {
    lateinit var viewBinder:ActivityWeatherBinding
    lateinit var location : String
    lateinit var name : String

    private val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        viewBinder = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)


        location = intent.getStringExtra("location")?:"101010100"
        name = intent.getStringExtra("name").toString()


        viewModel.searchWeatherInfo(location)

        viewModel.weatherInfoLiveData.observe(this){ result->
            Log.d("debug in WeatherActivity",result.toString())
            val weatherData = result.getOrNull()
            if (weatherData != null) {
                val parentLayout = viewBinder.layoutForecast.forecastLayout
                parentLayout.removeAllViews()
                for(weatherInfo in weatherData.weatherInfo){
                    val newViewBinder = ForecastItemBinding.inflate(LayoutInflater.from(this),parentLayout,false)
                    newViewBinder.dataInfo.text = weatherInfo.fxDate
                    newViewBinder.skyDay.setImageResource(resources.getIdentifier("_${weatherInfo.iconDay}","drawable",packageName))

                    newViewBinder.skyDayInfo.text = weatherInfo.textDay
                    newViewBinder.temperatureInfo.text = "${weatherInfo.tempMin}~${weatherInfo.tempMax}℃"
                    parentLayout.addView(newViewBinder.root)
                }
                viewBinder.layoutNow.apply{
                    placeName.text = name
                    currentTemp.text = weatherData.nowData.temp+"℃"
                    currentSky.text = weatherData.nowData.text
                    currentAQI.text = "湿度：${weatherData.nowData.humidity}%"
                    nowLayout.setBackgroundResource(R.drawable.bg_partly_cloudy_day)
                }

                viewBinder.lifeIndex.apply {
                    coldRiskText.text = weatherData.lifeIndex[8].category
                    dressingText.text = weatherData.lifeIndex[2].category
                    ultravioletText.text = weatherData.lifeIndex[4].category
                    carWashingText.text = weatherData.lifeIndex[1].category
                }
            }
        }


    }
}