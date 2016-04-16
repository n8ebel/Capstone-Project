package com.n8.intouch.data

import com.firebase.client.FirebaseError
import com.n8.intouch.model.ScheduledEvent

interface EventsDataManager {

    interface Listener {
        fun onScheduledEventAdded(event: ScheduledEvent, index:Int)
        fun onScheduledEventRemoved(event: ScheduledEvent, index:Int)
    }

    fun addScheduledEventListener(listener: Listener)

    fun removeScheduledEventListener(listener: Listener)

    fun getEvents(function:(List<ScheduledEvent>) -> Unit)

    fun addEvent(
            startDateTimestamp:Long = -1L,

            startDateHour:Int = -1,

            startDateMin:Int = -1,

            repeatInterval:Int = -1,  // value such as '1' or '3'

            repeatDuration:Long = -1L,  // value such as week.inMillis()

            scheduledMessage:String = "",

            phoneNumber:String = "",

            function:(event:ScheduledEvent?, FirebaseError?) -> Unit)

    fun removeEvent(event:ScheduledEvent, function:(Boolean, FirebaseError?) -> Unit)

    fun getEvent(id:String, function:(event:ScheduledEvent?, FirebaseError?) -> Unit)
}