package com.example.weatherapp.weatherinfo.viewmodel

import com.example.weatherapp.weatherinfo.model.WeatherInfoData

interface WeatherInfoCallbacks
{
    fun onWeatherInfoDownloadSuccess(weatherInfoDataList : ArrayList<WeatherInfoData>)
    fun onLocationSelected(location : String)
    fun onMetricsSelected(metricks : String)
    fun onYearSelected(year : String)
    fun notifyUser(message : String)
}