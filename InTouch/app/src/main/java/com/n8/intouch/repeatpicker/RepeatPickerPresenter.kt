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

    var startHour:Int = 0

    var startMin:Int = 0

    var interval:Int = 1

    var duration:Long = DateUtils.DAY_IN_MILLIS

    override fun onViewCreated() {
        view.setTimeText(context.getString(R.string.choose_a_time))
    }

    override fun onDateSelectorClicked() {
        TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePickerView, hour, minute ->
                    startHour = hour
                    startMin = minute

                    view.setTimeText(formatTime(hour, minute))
                    view.setContinueButtonVisible(true)
                }, startHour, startMin, false).
                show()
    }

    override fun onContinueClicked() {
        listener.onRepeatScheduleSelected(startHour, startMin, interval, duration)
    }

    // region Private Methods

    fun formatTime(hour: Int, min: Int): String {
        return TIME_FORMAT.format(Date(1960, 1, 1, hour, min, 0))
    }

    // endregion Private Methods

}
