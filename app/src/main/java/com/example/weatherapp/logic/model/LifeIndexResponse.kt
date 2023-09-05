package com.example.weatherapp.logic.model

import com.example.weatherapp.logic.entity.LifeIndex
import com.google.gson.annotations.SerializedName

data class LifeIndexResponse(val code:String,val updateTime:String,val fxLink:String,@SerializedName("daily") val data:ArrayList<DailyDate>)


data class DailyDate(val date:String,
                     val type:String,
                     val name:String,
                     val level:String,
                     val category:String,
                     val text:String)

fun DailyDate.toLifeIndex(id:String):LifeIndex{
    return LifeIndex(id, date, type, name, level, category, text)
}