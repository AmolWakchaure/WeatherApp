package com.example.weatherapp.weatherinfo.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.other.Functions
import com.example.weatherapp.weatherinfo.model.WeatherInfoData

class WeatherInfoAdapter (val weatherInfoList: ArrayList<WeatherInfoData>) : RecyclerView.Adapter<WeatherInfoAdapter.ViewHolder>()
{
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItems(weatherInfoList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int
    {
        return weatherInfoList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(user: WeatherInfoData)
        {
            val yearTxt = itemView.findViewById(R.id.yearTxt) as TextView
            val valueTxt = itemView.findViewById(R.id.valueTxt) as TextView
            val valueUnitTxt = itemView.findViewById(R.id.valueUnitTxt) as TextView
            val imageStatus = itemView.findViewById(R.id.imageStatus) as ImageView


            var metricsData = user.metrics

            if(metricsData.equals("Tmin") || metricsData.equals("Tmax"))
            {
                yearTxt.text = Functions.returnMonthName(user.month)+" "+user.year
                valueTxt.text = user.value
                valueUnitTxt.text = "\u2103"
                imageStatus.setImageResource(R.drawable.ic_sun)
            }
            else
            {
                yearTxt.text = Functions.returnMonthName(user.month)+" "+user.year
                valueTxt.text = user.value
                valueUnitTxt.text = "mm"
                imageStatus.setImageResource(R.drawable.ic_rainfall)
            }


        }

    }
}