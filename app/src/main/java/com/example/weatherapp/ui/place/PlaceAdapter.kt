package com.example.weatherapp.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.PlaceItemBinding
import com.example.weatherapp.logic.model.Location
import com.example.weatherapp.ui.weather.WeatherActivity

class PlaceAdapter(private val context: PlaceFragment, private val placeList:ArrayList<Location>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    class ViewHolder(viewBinder:PlaceItemBinding):RecyclerView.ViewHolder(viewBinder.root){
        val placeName = viewBinder.placeName
        val placeAddress = viewBinder.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinder = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(viewBinder)

        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.layoutPosition
            val intent = Intent(parent.context,WeatherActivity::class.java)
            Log.d("debug out", placeList[position].toString())
            intent.putExtra("location",placeList[position].id)
            intent.putExtra("name",placeList[position].name)
            context.startActivity(intent)
//            context.activity?.finish()
        }

        return viewHolder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeAddress.text = place.adm1 + place.adm2
        holder.placeName.text = place.name
    }
}