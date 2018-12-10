package com.example.weatherapp.weatherinfo.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.GridLayout
import android.widget.LinearLayout
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.other.Functions
import com.example.weatherapp.weatherinfo.adapters.WeatherInfoAdapter
import com.example.weatherapp.weatherinfo.model.WeatherInfoData
import com.example.weatherapp.weatherinfo.viewmodel.WeatherInfoCallbacks
import com.example.weatherapp.weatherinfo.viewmodel.WeatherInfoFactory
import com.example.weatherapp.weatherinfo.viewmodel.WeatherInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*

class WeatherInfoActivity : AppCompatActivity(),WeatherInfoCallbacks {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val activityWeatherInf = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        //insatantiate new ViewModels
        activityWeatherInf.weatherInfoViewModel = ViewModelProviders.of(this, WeatherInfoFactory(this,this,this)).get(WeatherInfoViewModel::class.java)

    }


    override fun onWeatherInfoDownloadSuccess(weatherInfoDataList: ArrayList<WeatherInfoData>) {


        //weatherInfoRecyc.layoutManager = LinearLayoutManager(MyApplication.context, LinearLayout.VERTICAL, false)
        weatherInfoRecyc.layoutManager = GridLayoutManager(MyApplication.context,  3)
        val adapter = WeatherInfoAdapter(weatherInfoDataList);
        weatherInfoRecyc.adapter = adapter
        adapter.notifyDataSetChanged()

    }
    override fun onLocationSelected(location: String) {

        locationText.setText(location)
        locationTxt.setText(location)

    }

    override fun onMetricsSelected(metricks: String) {

        if(metricks.equals("Max Temperature") || metricks.equals("Min Temperature"))
        {
            imageStatus.setImageResource(R.drawable.ic_sun)
        }
        else
        {
            imageStatus.setImageResource(R.drawable.ic_rainfall)
        }
        metricsText.setText(metricks)
        unitTxt.setText(metricks)

    }
    override fun onYearSelected(year: String) {

        yearTxt.setText(year)
        yearText.setText(year)
    }
    override fun notifyUser(message: String) {

       // Functions.t(message)

    }

}
