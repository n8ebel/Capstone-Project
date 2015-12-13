package com.n8.intouch.model

import android.graphics.Bitmap

/**
 * Created by n8 on 12/8/15.
 */

data class Contact(val name:String, val thumbnail: Bitmap? = null, val events:List<Event> = listOf())

open class Event(val type:String, val label:String?, val date:String){
    override fun toString(): String {
        return "$type  --  $date"
    }
}