package com.example.weatherapp.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.example.weatherapp.MyApplication
import android.content.DialogInterface
import android.R.array
import android.support.v7.app.AlertDialog
import com.example.weatherapp.R
import com.example.weatherapp.weatherinfo.viewmodel.ReturnData


class Functions
{
    companion object {

        fun returnMonthName(monthNumber : String ) : String
        {
            var  monthName : String
            if(monthNumber.equals("1"))
            {
                monthName = "Jan"
            }
            else if(monthNumber.equals("2"))
            {
                monthName = "Feb"
            }
            else if(monthNumber.equals("3"))
            {
                monthName = "Mar"
            }
            else if(monthNumber.equals("4"))
            {
                monthName = "Apr"
            }
            else if(monthNumber.equals("5"))
            {
                monthName = "May"
            }
            else if(monthNumber.equals("6"))
            {
                monthName = "Jun"
            }
            else if(monthNumber.equals("7"))
            {
                monthName = "Jul"
            }
            else if(monthNumber.equals("8"))
            {
                monthName = "Aug"
            }
            else if(monthNumber.equals("9"))
            {
                monthName = "Sep"
            }
            else if(monthNumber.equals("10"))
            {
                monthName = "Oct"
            }
            else if(monthNumber.equals("11"))
            {
                monthName = "Nov"
            }
            else if(monthNumber.equals("12"))
            {
                monthName = "Dec"
            }
            else
            {
                monthName = "Date not found"
            }
            return monthName
        }
        //to display toast
        fun t(message : String) {
            Toast.makeText(MyApplication.context,message,Toast.LENGTH_LONG).show()
        }

        //to check network or wifi connection
        fun isNetworkAvailable(): Boolean
        {
            val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }

        //dialog for location,metrics,year
        fun returnWeatherInfoInput(selectStatus : String,context: Context,returnData : ReturnData)
        {
            var  singleChoiceItems : Array<String>
            var  title : String

            try
            {
                if(selectStatus.equals("location"))
                {
                    singleChoiceItems = MyApplication.context.resources.getStringArray(R.array.locationArray)
                    title = "Select Location"
                }
                else
                {
                    singleChoiceItems = MyApplication.context.resources.getStringArray(R.array.metricsArray)
                    title = "Select Metrics"
                }

                // Initialize a new instance of alert dialog builder object
                val builder = AlertDialog.Builder(context)
                // Set a title for alert dialog
                builder.setTitle(title)
                // Set items form alert dialog
                builder.setItems(singleChoiceItems,{_, which ->
                    // Get the dialog selected item
                    var selectedItem = singleChoiceItems[which]
                    returnData.returnData(selectedItem)
                })
                // Create a new AlertDialog using builder object
                val dialog = builder.create()
                // Finally, display the alert dialog
                dialog.show()
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }

        }
    }

}