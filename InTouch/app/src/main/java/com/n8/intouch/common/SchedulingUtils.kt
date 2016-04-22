package com.n8.intouch.common

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.PluralsRes
import android.text.format.DateUtils
import com.n8.intouch.R
import com.n8.intouch.model.ScheduledEvent
import java.text.SimpleDateFormat
import java.util.*

class SchedulingUtils {
    companion object {

        // TODO make this locale safe
        val TIME_FORMAT = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        val DATE_FORMAT = SimpleDateFormat.getDateInstance()

        fun getPluralsStringIdForDuration(@NonNull context: Context, @NonNull event:ScheduledEvent) : Int {
            return when (event.repeatDuration) {
                DateUtils.DAY_IN_MILLIS -> R.plurals.days_format
                DateUtils.WEEK_IN_MILLIS -> R.plurals.weeks_format
                DateUtils.YEAR_IN_MILLIS -> R.plurals.years_format
                else -> throw IllegalStateException("Invalid duration value: " + event.repeatDuration)
            }
        }

        fun getDisplayStringForDuration(@NonNull context:Context, duration:Long) : String {
            return when (duration) {
                DateUtils.DAY_IN_MILLIS -> context.getString(R.string.days)
                DateUtils.WEEK_IN_MILLIS -> context.getString(R.string.weeks)
                DateUtils.YEAR_IN_MILLIS -> context.getString(R.string.years)
                else -> throw IllegalStateException("Invalid duration value: " + duration)
            }
        }

        fun getTimeDisplayString(hour:Int, min:Int) : String {
            val cal = GregorianCalendar(1960, 1, 1, hour, min)
            return TIME_FORMAT.format(cal.time)
        }

        fun getDateTimeDisplayString(time:Long) : String {
            return DATE_FORMAT.format(Date(time))
        }
    }
}