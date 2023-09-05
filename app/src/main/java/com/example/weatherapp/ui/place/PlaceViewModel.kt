package com.example.weatherapp.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.weatherapp.logic.Repository
import com.example.weatherapp.logic.model.Location

class PlaceViewModel :ViewModel() {
    private val searchLiveData = MutableLiveData<String>()



    val placeList = ArrayList<Location>()

    val placeLiveData = searchLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

}