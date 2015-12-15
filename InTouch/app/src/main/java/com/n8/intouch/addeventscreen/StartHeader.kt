package com.n8.intouch.addeventscreen

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.widget.TextView
import com.n8.intouch.R

/**
 * Created by n8 on 12/14/15.
 */
class StartHeader : CardView {

    lateinit var titleTextView: TextView

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setTitle(title:String){
        titleTextView.text = title
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        titleTextView = findViewById(R.id.titleTextView) as TextView
    }
}