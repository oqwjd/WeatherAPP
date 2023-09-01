package com.example.weatherapp.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val code:String,val location:List<Location>,val refer:Refer)

data class Location(
    val name:String,
    val id:String,
    val lat:String,
    val lon:String,
    val adm1:String,
    val adm2:String,
    val country:String,
    val tz:String,
    @SerializedName("utcOffset") val utc:String,
    val isDst:String,
    val type:String,
    val rank:String,
    val fxLink:String)

data class Refer(val source:List<String>,val license:List<String>)

