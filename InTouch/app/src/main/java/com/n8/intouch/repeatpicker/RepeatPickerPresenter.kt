package com.n8.intouch.repeatpicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateUtils
import com.n8.intouch.R
import java.text.DateFormat
import java.util.*

class RepeatPickerPresenter(val context: Context, val view:Contract.View, val listener:RepeatPickerFragment.Listener) : Contract.UserInteractionListener {

    val TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT)

    var startHour:Int = -1

    var startMin:Int = -1

    var mFrequency:Int = -1

    var mInterval:Long = DateUtils.DAY_IN_MILLIS

    override fun onViewCreated() {
        view.setTimeText(context.getString(R.string.choose_a_time))
    }

    override fun onDateSelectorClicked() {
        val initialHour = if (startHour >= 0) startHour else 0
        val initialMin = if (startMin >= 0) startMin else 0

        TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePickerView, hour, minute ->
                    startHour = hour
                    startMin = minute

                    view.setTimeText(formatTime(hour, minute))
                    updateContinueButton()
                }, initialHour, initialMin, false).
                show()
    }

    override fun onContinueClicked() {
        listener.onRepeatScheduleSelected(startHour, startMin, mFrequency, mInterval)
    }

    override fun onFrequencySelected(frequency: Int) {
        mFrequency = frequency
        updateContinueButton()
    }

    override fun onIntervalSelected(interval: Long) {
        mInterval = interval
        updateContinueButton()
    }

    // region Private Methods

    fun formatTime(hour: Int, min: Int): String {
        return TIME_FORMAT.format(Date(1960, 1, 1, hour, min, 0))
    }

    fun updateContinueButton() {
        if (mFrequency <= 0 || startHour < 0 || startMin < 0) {
            view.setContinueButtonVisible(false)
        } else {
            view.setContinueButtonVisible(true)
        }
    }

    // endregion Private Methods

}
