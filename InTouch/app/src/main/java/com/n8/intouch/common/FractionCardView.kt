package com.n8.intouch.common

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.Log
import android.view.WindowManager

open class FractionCardView : CardView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun getYFraction() : Float {
        var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (height == 0) {
            return 0f
        } else {
            return translationY / height
        }
    }

    fun setYFraction(fraction: Float) {
        var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (height == 0) {
            translationY = 0f
            Log.d("foo", "setYFraction: $fraction translationY = $translationY")
        } else {
            translationY = fraction * height
            Log.d("foo", "setYFraction: $fraction translationY = $translationY")
        }
    }
}