package com.example.weatherapp.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.PlaceItemBinding
import com.example.weatherapp.logic.model.Location

class PlaceAdapter(val context: PlaceFragment, val placeList:List<Location>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    class ViewHolder(viewBinder:PlaceItemBinding):RecyclerView.ViewHolder(viewBinder.root){
        val placeName = viewBinder.placeName
        val placeAddress = viewBinder.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinder = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewHolder = ViewHolder(viewBinder)
        return viewHolder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeAddress.text = place.adm1 + place.adm2
        holder.placeName.text = place.name
    }
}