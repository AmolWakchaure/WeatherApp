package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.weatherapp.sqlite.DBHelper
import java.security.AccessControlContext

class MyApplication : Application()
{
    companion object {

        lateinit var context: Context
        var db: DBHelper? = null
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized var instance: MyApplication? = null
            private set

    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        instance = this
        //create db object
        if(db == null)
        {
            db = DBHelper(context)
            db!!.writableDatabase
        }

    }
    //create volley request queue
    val requestQueue: RequestQueue? = null
        get()
        {
            if (field == null)
            {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }
    //add request queue to the volley
    fun <T> addToRequestQueue(request: Request<T>)
    {
        request.tag = TAG
        request.retryPolicy = DefaultRetryPolicy(50000, 0, 0F)
        requestQueue?.add(request)
    }
}