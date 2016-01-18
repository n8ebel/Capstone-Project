package com.n8.intouch.datepicker

import android.app.DatePickerDialog
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.n8.intouch.R
import com.n8.intouch.common.FractionCardView
import com.n8.intouch.model.Event
import java.text.ParseException
import java.util.*

class DatePickerCard : FractionCardView {

    interface DateClickedListener {
        fun onDateClicked(event:Event)

        fun onCustomClicked()
    }

    lateinit var continueButton: FloatingActionButton

    lateinit var datesList: ListView

    lateinit var dateClickListener: DateClickedListener

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        continueButton = findViewById(R.id.continueFAB) as FloatingActionButton
    }

    fun setContinueButtonEnabled(enabled: Boolean) {
        if (enabled) continueButton.show() else continueButton.hide()
    }

    fun setEvents(events: List<Event>) {
        var spinnerEvents = ArrayList<Event>(events)
        spinnerEvents.add(CustomDateEvent(context.getString(R.string.custom_date)))
        datesList.adapter = ArrayAdapter<Event>(context, android.R.layout.simple_list_item_1, spinnerEvents)
    }

    // region Private Methods

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.date_picker_card, this, true)

        datesList = findViewById(R.id.listView) as ListView
        datesList.setOnItemClickListener { adapterView, view, position, l ->
            if (position == datesList.adapter.count - 1) {
                dateClickListener.onCustomClicked()
            } else {
                dateClickListener.onDateClicked(datesList.adapter.getItem(position) as Event)
            }
        }
    }

    // endregion Private Methods

    private class CustomDateEvent(val msg:String) : Event("Custom", "Custom", "1900/01/01") {

        override fun toString(): String {
            return msg
        }
    }
}