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
import android.support.design.widget.Snackbar
import android.text.TextUtils


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

               Functions.t("Select metrics")
               //check weather location is empty
               if(TextUtils.isEmpty(weatherModel.getLocation()))
               {
                   //download weather details
                   listner.onLocationSelected(location)
                   weatherModel.setLocation(location)
                   downloadWeatherDetails()
               }
               else
               {

                   listner.onLocationSelected(weatherModel.getLocation()!!)
                   weatherModel.setLocation(location)

               }

           }

       })

    }
    //to perform onclick for select metricks
    fun onSelectMetricks(view : View)
    {

        Functions.returnWeatherInfoInput("metricks",context, object : ReturnData{
            override fun returnData(metricks: String) {

                Functions.t("Select year")
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

                    //download weather details
                    downloadWeatherDetails()
                }
                else{
                    //display weather details
                    setYearWiseData()
                }
            }

        });
    }
    //download weather details firs time
    fun downloadWeatherDetailsFirstTime(){


        listner.onLocationSelected("England")
        listner.onMetricsSelected("Max Temperature")
        listner.onYearSelected("2017")
        metricksInput = "Tmax"

        //check weather details available in local db else download it
        weatherInfoList = tbl_WEATHER_INFO.checkWeatherDataExists(metricksInput!!,"England","2017")

        if(weatherInfoList.isEmpty())
        {
            //download weather details
            weatherModel.setLocation("England")
            weatherModel.setYear("2017")
            downloadWeatherDetails()
        }
        else
        {
            //display weather details
            weatherModel.setLocation("England")
            weatherModel.setYear("2017")
            setYearWiseData()
        }


    }
    //download weather details
    fun downloadWeatherDetails()
    {
        //check internet or wifi enable or disable
        if(Functions.isNetworkAvailable()){
            downloadWeatherInfo(metricksInput!!,weatherModel.getLocation()!!)
        }
        else
        {
            //notify user when no connection
            Functions.t(MyApplication.context.resources.getString(R.string.checkNetwork))
        }
    }
    //select year wise weather info
    private fun setYearWiseData()
    {

        var location =  weatherModel.getLocation()!!
        var year =  weatherModel.getYear()!!

        weatherInfoList.clear()
        //get weather info year wise from sqlite
        weatherInfoList = tbl_WEATHER_INFO.checkWeatherDataExists(metricksInput!!,location,year)
        listner.onWeatherInfoDownloadSuccess(weatherInfoList)

    }


    //to perform onclick for select year
    fun onSelectYear(view : View)
    {

        if(yearsData.isEmpty())
        {
            //get year data from sqlite when metrics and location is selected
            yearsData = tbl_WEATHER_INFO.checkYearDataExists(metricksInput!!,weatherModel.getLocation()!!)
        }


        //sort year data
        Collections.sort(yearsData,Collections.reverseOrder())
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
            //progress dialog for user to wait
            var progressDialog = ProgressDialog(context);
            progressDialog.setMessage("Downloading weather details...")
            progressDialog.setCancelable(false)
            progressDialog.show()


            // make request to get weather info
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
                            //notify user when request time out
                            val snackbar = Snackbar
                                .make(WeatherInfoActivity.mainLayoutLi as View, "Oops ! request timed out try again.", Snackbar.LENGTH_LONG)
                                .setAction("Try again") {

                                    //try again when server request timed out
                                    downloadWeatherInfo(metricks,location)
                                }

                            snackbar.show()

                        }
                    }

                )
                {

                    //parse weather info response in worker thread and store into local db
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
                           //get year data from sqlite
                           yearsData = tbl_WEATHER_INFO.checkYearDataExists(metricks,location)

                           //update ui with downloaded weather info
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