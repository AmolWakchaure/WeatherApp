package com.example.weatherapp.weatherinfo.viewmodel

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.example.weatherapp.MyApplication
import com.example.weatherapp.R
import com.example.weatherapp.other.Constants
import com.example.weatherapp.other.Functions
import com.example.weatherapp.sqlite.tbl_WEATHER_INFO
import com.example.weatherapp.weatherinfo.model.WeatherInfoData
import com.example.weatherapp.weatherinfo.model.WeatherModel
import com.example.weatherapp.weatherinfo.view.WeatherInfoActivity
import org.json.JSONArray
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import java.nio.file.Files.size
import android.widget.Toast







class WeatherInfoViewModel (private val listner : WeatherInfoCallbacks,val context : Context,val activity : WeatherInfoActivity) : ViewModel()
{

    var weatherInfoList : ArrayList<WeatherInfoData>
    var yearsData = ArrayList<String>()

    private val weatherModel : WeatherModel

    var metricksInput : String? = null
    //create cunstructor for initialise objects
    init {

        weatherModel = WeatherModel("","","")
        weatherInfoList = ArrayList<WeatherInfoData>()
        //dowload data first time
        downloadWeatherDetailsFirstTime()

    }

    //to perform onclick for select location
    fun onSelectLocation(view : View)
    {


       Functions.returnWeatherInfoInput("location",context, object : ReturnData{
           override fun returnData(location: String) {



               listner.onLocationSelected(location)
               weatherModel.setLocation(location)
           }

       })

    }
    //to perform onclick for select metricks
    fun onSelectMetricks(view : View)
    {
        Functions.returnWeatherInfoInput("metricks",context, object : ReturnData{
            override fun returnData(metricks: String) {

                listner.onMetricsSelected(metricks)
                weatherModel.setMetricks(metricks)


                if(metricks.equals("Max Temperature"))
                {
                    metricksInput = "Tmax"
                }
                else if(metricks.equals("Min Temperature"))
                {
                    metricksInput = "Tmin"
                }
                else if(metricks.equals("Rainfall"))
                {
                    metricksInput = "Rainfall"
                }

                var location = weatherModel.getLocation()!!
                //check year is present or not
                yearsData = tbl_WEATHER_INFO.checkYearDataExists(metricksInput!!,location)

                if(yearsData.isEmpty()){
                    downloadWeatherDetails()
                }
                else{
                    setYearWiseData()
                }
            }

        });
    }
    fun downloadWeatherDetailsFirstTime(){


        listner.onLocationSelected("England")
        listner.onMetricsSelected("Max Temperature")
        listner.onYearSelected("2017")
        metricksInput = "Tmax"

        weatherInfoList = tbl_WEATHER_INFO.checkWeatherDataExists(metricksInput!!,"England","2017")

        if(weatherInfoList.isEmpty())
        {
            weatherModel.setLocation("England")
            weatherModel.setYear("2017")
            downloadWeatherDetails()
        }
        else
        {
            weatherModel.setLocation("England")
            weatherModel.setYear("2017")
            setYearWiseData()
        }


    }
    fun downloadWeatherDetails()
    {
        //check internet or wifi enable or disable
        if(Functions.isNetworkAvailable()){
            downloadWeatherInfo(metricksInput!!,weatherModel.getLocation()!!)
        }
        else
        {
            Functions.t(MyApplication.context.resources.getString(R.string.checkNetwork))
        }
    }
    private fun setYearWiseData()
    {


        var location =  weatherModel.getLocation()!!
        var year =  weatherModel.getYear()!!

        /*Log.e("WEATHERAPP","location : "+location)
        Log.e("WEATHERAPP","year : "+year)
        Log.e("WEATHERAPP","metrics : "+metricksInput!!)*/

        weatherInfoList.clear()
        weatherInfoList = tbl_WEATHER_INFO.checkWeatherDataExists(metricksInput!!,location,year)
        listner.onWeatherInfoDownloadSuccess(weatherInfoList)

    }

    //var data: Array<String>? = null
    //to perform onclick for select year
    fun onSelectYear(view : View)
    {


        var arr = arrayOfNulls<String>(yearsData.size)
        arr = yearsData.toArray(arr)

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(context)
        // Set a title for alert dialog
        builder.setTitle("Select Year")
        // Set items form alert dialog
        builder.setItems(arr,{_, which ->
            // Get the dialog selected item
            var selectedYear = yearsData!![which]
            listner.onYearSelected(selectedYear)
            weatherModel.setYear(selectedYear)

            //get data from sqlite
            setYearWiseData()
        })
        // Create a new AlertDialog using builder object
        val dialog = builder.create()
        // Finally, display the alert dialog
        dialog.show()
    }

    //to download weather info details
    fun downloadWeatherInfo(metricks : String,location : String)
    {

        try
        {
            var progressDialog = ProgressDialog(context);
            progressDialog.setMessage("Loading...")
            progressDialog.setCancelable(false)
            progressDialog.show()


            val stringRequest = object : StringRequest
                    (

                    Request.Method.GET,
                    Constants.WEB_URL+""+metricks+"-"+location+".json",
                    Response.Listener<String>{

                            response ->
                        progressDialog.dismiss()
                        //Log.e("WEATHER_APP","response : "+response)
                    },
                    object : Response.ErrorListener
                    {
                        override fun onErrorResponse(volleyError: VolleyError)
                        {

                            progressDialog.dismiss()
                            listner.notifyUser("Volley : "+volleyError)

                        }
                    }

                )
                {

                   override fun parseNetworkResponse(response: NetworkResponse?): Response<String>
                   {

                       //parse json response
                       val weatherInfoResponse = String(response?.data ?: ByteArray(0),Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))

                       var VALUE = "NA"
                       var YEAR  = "NA"
                       var MONTH = "NA"

                       if(weatherInfoResponse.length > 0 && weatherInfoResponse != null)
                       {
                           var weatherJSONArray = JSONArray(weatherInfoResponse);
                           for (i in 0 until weatherJSONArray.length())
                           {
                               var weatherJsonObject = weatherJSONArray.getJSONObject(i)

                               if(weatherJsonObject.has("value") && !weatherJsonObject.isNull("value"))
                               {
                                   VALUE = weatherJsonObject.getString("value")
                               }
                               if(weatherJsonObject.has("year") && !weatherJsonObject.isNull("year"))
                               {
                                   YEAR = weatherJsonObject.getString("year")
                               }
                               if(weatherJsonObject.has("month") && !weatherJsonObject.isNull("month"))
                               {
                                   MONTH = weatherJsonObject.getString("month")
                               }

                               //insert weather info into sqlite
                               tbl_WEATHER_INFO.putAllWeatherInfo(VALUE,YEAR,MONTH,location,metricks)


                           }
                           yearsData = tbl_WEATHER_INFO.checkYearDataExists(metricks,location)
                           //setYearWiseData()
                           //Log.i("response", weatherInfoResponse)

                           activity.runOnUiThread(
                                   object : Runnable
                                   {
                                       override fun run()
                                       {
                                           progressDialog.dismiss()
                                           setYearWiseData()
                                       }
                                   }
                               )



                       }

                       return super.parseNetworkResponse(response)
                   }
                }

                //adding request to queue
                MyApplication.instance?.addToRequestQueue(stringRequest)

        }
        catch (e : Exception) {

            e.printStackTrace()
        }
    }


}