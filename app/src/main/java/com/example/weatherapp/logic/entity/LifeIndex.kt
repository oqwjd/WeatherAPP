package com.example.weatherapp.logic.entity

import androidx.room.Entity
import com.example.weatherapp.logic.model.DailyDate

@Entity(tableName = "life-index", primaryKeys = ["id","type"])
data class LifeIndex(val id:String,
                     val date:String,
                     val type:String,
                     val name:String,
                     val level:String,
                     val category:String,
                     val text:String
)
