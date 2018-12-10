package com.example.weatherapp.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.weatherapp.other.Constants

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(tbl_WEATHER_INFO.CREATE_TABLE)

        } catch (e: SQLiteException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        internal val DATABASE_NAME = Constants.DATABASE_NAME
        internal val DATABASE_VERSION = Constants.DATABASE_VERSION

    }

}