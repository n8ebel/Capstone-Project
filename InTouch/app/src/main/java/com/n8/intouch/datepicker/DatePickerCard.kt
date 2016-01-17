package com.n8.intouch.datepicker

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.n8.intouch.R
import com.n8.intouch.common.FractionCardView

/**
 * Created by n8 on 12/14/15.
 */
class DatePickerCard : FractionCardView {

    lateinit var continueButton: FloatingActionButton

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()

        continueButton = findViewById(R.id.continueFAB) as FloatingActionButton
    }

    fun setContinueButtonEnabled(enabled: Boolean) {
        if (enabled) continueButton.show() else continueButton.hide()
    }
}