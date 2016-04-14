package com.n8.intouch.data

import com.firebase.client.FirebaseError
import com.n8.intouch.model.ScheduledEvent

interface EventsDataManager {
    fun getEvents(function:(List<ScheduledEvent>) -> Unit)

    fun addEvent(
            startDateTimestamp:Long = -1L,

            startDateHour:Int = -1,

            startDateMin:Int = -1,

            repeatInterval:Int = -1,  // value such as '1' or '3'

            repeatDuration:Long = -1L,  // value such as week.inMillis()

            scheduledMessage:String = "",

            function:(Boolean, FirebaseError?) -> Unit)

    fun removeEvent(event:ScheduledEvent, function:(Boolean, FirebaseError?) -> Unit)
}