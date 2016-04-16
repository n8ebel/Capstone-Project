package com.n8.intouch.model

import android.graphics.Bitmap
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by n8 on 12/8/15.
 */

data class Contact(val name:String, val thumbnail: Bitmap? = null, val events:List<SystemEvent> = listOf())

open class SystemEvent(val type:String = "", val label:String? = "", private val dateString:String = "", val message:String = ""){


    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

    val date:Long

    init {
        try {
            date = DATE_FORMAT.parse(dateString).time
        } catch (e: Exception){
            date = 0
        }
    }

    override fun toString(): String {
        return "$type  --  $date"
    }
}

data class ScheduledEvent(
        val id:String = "",

        val startDateTimestamp:Long = -1L,

        val startDateHour:Int = -1,

        val startDateMin:Int = -1,

        val repeatInterval:Int = -1,  // value such as '1' or '3'

        val repeatDuration:Long = -1L,  // value such as week.inMillis()

        val scheduledMessage:String = ""

)