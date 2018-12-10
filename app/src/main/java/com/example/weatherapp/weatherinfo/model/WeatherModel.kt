package com.example.weatherapp.weatherinfo.model

import android.databinding.BaseObservable

class WeatherModel(private var location : String,private var metricks : String,private var year : String) : BaseObservable(){



    //location
    fun setLocation(location : String)
    {
        this.location = location


    }
    fun getLocation(): String?
    {
        return location

    }
    //metricks
    fun setMetricks(metricks : String)
    {
        this.metricks = metricks

    }
    fun getMetricks(): String?
    {
        return metricks

    }
    //year
    fun setYear(year : String)
    {
        this.year = year

    }
    fun getYear(): String?
    {
        return year

    }
}