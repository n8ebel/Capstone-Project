package com.n8.intouch.repeatpicker

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import com.n8.intouch.R
import com.n8.intouch.common.FractionCardView

class RepeatPickerCard : FractionCardView {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    // region Private Functions

    private fun init(){
        LayoutInflater.from(context).inflate(R.layout.repeat_picker_card, this, true)
    }

    // endregion Private Functions
}