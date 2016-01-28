package com.n8.intouch.model

import android.graphics.Bitmap
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by n8 on 12/8/15.
 */

data class Contact(val name:String, val thumbnail: Bitmap? = null, val events:List<Event> = listOf())

open class Event(val type:String, val label:String?, private val dateString:String){

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