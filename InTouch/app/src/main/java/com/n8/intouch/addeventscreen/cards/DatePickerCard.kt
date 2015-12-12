package com.n8.intouch.addeventscreen.cards

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater

import com.n8.intouch.R

/**
 * View allowing user to choose a date
 */
class DatePickerCard : CardView {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        LayoutInflater.from(context).inflate(R.layout.date_picker_card, this, true)

        radius = context.resources.getDimension(R.dimen.card_corner_radius)
        cardElevation = context.resources.getDimension(R.dimen.card_elevation_resting)
    }
}