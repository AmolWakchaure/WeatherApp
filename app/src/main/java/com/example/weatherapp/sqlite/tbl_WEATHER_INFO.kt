package com.example.weatherapp.sqlite

import android.content.ContentValues
import android.database.Cursor
import com.example.weatherapp.MyApplication
import com.example.weatherapp.weatherinfo.model.WeatherInfoData
import java.util.ArrayList

class tbl_WEATHER_INFO {
    companion object {
        //table name
        val TABLE_NAME: String = "tbl_WEATHER_INFO"

        //table fields
        val ID: String = "id"
        val VALUE: String = "value"
        val YEAR: String = "year"
        val MONTH: String = "month"
        val LOCATION: String = "location"
        val METRICKS: String = "metricks"



        var CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VALUE + " TEXT, "
                + YEAR + " TEXT, "
                + MONTH + " TEXT, "
                + LOCATION + " TEXT, "
                + METRICKS + " TEXT)")


        //insert weather info into table
        fun putAllWeatherInfo(value: String, year: String, month: String, location: String, metricks: String) {

            val db = MyApplication.db!!.getWritableDatabase()

            val values = ContentValues()
            values.put(VALUE, value)
            values.put(YEAR, year)
            values.put(MONTH, month)
            values.put(LOCATION, location)
            values.put(METRICKS, metricks)

            db.insert(TABLE_NAME, null, values)

        }

        fun checkWeatherDataExists(metricks : String,location : String,year : String): ArrayList<WeatherInfoData> {

            var cursor: Cursor?
            val WEATHER_INFO = ArrayList<WeatherInfoData>()
            try {

                val uQuery = "SELECT * FROM $TABLE_NAME WHERE $LOCATION = '"+location+"' AND $METRICKS = '"+metricks+"' AND $YEAR = '"+year+"'"
                cursor = MyApplication.db!!.getReadableDatabase().rawQuery(uQuery, null)

                if(cursor!!.count > 0)
                {
                    while (cursor!!.moveToNext())
                    {
                        val VALUE = cursor!!.getString(cursor.getColumnIndex(VALUE))
                        val YEAR = cursor!!.getString(cursor.getColumnIndex(YEAR))
                        val MONTH = cursor!!.getString(cursor.getColumnIndex(MONTH))
                        val METRICKS = cursor!!.getString(cursor.getColumnIndex(METRICKS))

                        WEATHER_INFO.add(WeatherInfoData(VALUE,YEAR,MONTH,METRICKS))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return WEATHER_INFO
        }
        fun checkYearDataExists(metricks : String,location : String): ArrayList<String> {

            var cursor: Cursor?
            var yearsData = ArrayList<String>()
            try {

                val uQuery = "SELECT DISTINCT $YEAR FROM $TABLE_NAME WHERE $LOCATION = '"+location+"' AND $METRICKS = '"+metricks+"'"
                cursor = MyApplication.db!!.getReadableDatabase().rawQuery(uQuery, null)

                if(cursor!!.count > 0)
                {

                    while (cursor!!.moveToNext())
                    {
                        val YEAR = cursor!!.getString(cursor.getColumnIndex(YEAR))
                        yearsData.add(YEAR)

                    }
                }



            } catch (e: Exception) {
                e.printStackTrace()
            }

            return yearsData
        }
        /*fun selectContentData(): Cursor {

            val db = MyApplication.db!!.getReadableDatabase()

            val cursor: Cursor
            val uQuery = "SELECT DISTINCT $ADD_NAME,$ADD_TYPE FROM $TABLE_NAME"
            cursor = db.rawQuery(uQuery, null)
            return cursor

        }

        fun selectContent()  : ArrayList<AddDetails>
        {

            var cursor: Cursor? = null
            val CONTENT_DATA = ArrayList<AddDetails>()
            try
            {
                cursor = selectContentData()
                if (cursor!!.count > 0)
                {
                    while (cursor!!.moveToNext())
                    {
                        val ADD_NAME = cursor!!.getString(cursor.getColumnIndex(ADD_NAME))
                        val ADD_TYPE = cursor.getString(cursor.getColumnIndex(ADD_TYPE))
                        CONTENT_DATA.add(AddDetails(ADD_NAME,ADD_TYPE))
                    }

                }

            }
            catch (e: Exception)
            {

            }
            return CONTENT_DATA

        }*/
    }
}