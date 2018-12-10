package com.example.weatherapp.weatherinfo.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.example.weatherapp.weatherinfo.view.WeatherInfoActivity

class WeatherInfoFactory(private val listner: WeatherInfoCallbacks,val context : Context,val activity: WeatherInfoActivity) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return WeatherInfoViewModel(listner,context,activity) as T
    }
}