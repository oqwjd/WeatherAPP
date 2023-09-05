package com.example.weatherapp.ui.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.caverock.androidsvg.SVG
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.databinding.ForecastItemBinding

class WeatherActivity : AppCompatActivity() {
    lateinit var viewBinder:ActivityWeatherBinding
    val location = intent.getStringExtra("location")?:"101010100"
    val name = intent.getStringExtra("name")

    private val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        viewBinder = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)

        viewBinder.layoutNow.placeName.text = name
        viewModel.searchWeatherInfo(location)

        viewModel.weatherInfoLiveData.observe(this){ result->
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