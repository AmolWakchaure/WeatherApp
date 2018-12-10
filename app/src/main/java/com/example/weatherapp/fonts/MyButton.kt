package com.example.weatherapp.fonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class MyButton : android.support.v7.widget.AppCompatButton {

    //vts.snystems.sns.vts.fonts.MyTextViewNormal
    constructor(context: Context) : super(context) {

        applyCustomFont()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        applyCustomFont()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

        applyCustomFont()
    }

    private fun applyCustomFont() {
        val tf = Typeface.createFromAsset(getContext().assets, "TitilliumWeb-Regular.ttf")
        typeface = tf
    }
}