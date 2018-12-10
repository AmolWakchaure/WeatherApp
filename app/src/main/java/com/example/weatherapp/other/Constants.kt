package com.example.weatherapp.other

interface Constants {

    companion object {


        //sqlite db details
        val DATABASE_NAME : String = "weather.db"
        val DATABASE_VERSION : Int = 1

        //api details
        val WEB_URL : String = "https://s3.eu-west-2.amazonaws.com/interview-question-data/metoffice/"
    }
}