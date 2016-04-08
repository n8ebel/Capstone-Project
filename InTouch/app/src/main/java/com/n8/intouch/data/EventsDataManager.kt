package com.n8.intouch.data

import com.firebase.client.FirebaseError
import com.n8.intouch.model.ScheduledEvent

interface EventsDataManager {
    fun getEvents(function:(List<ScheduledEvent>) -> Unit)

    fun addEvent(event:ScheduledEvent, function:(Boolean, FirebaseError?) -> Unit)
}