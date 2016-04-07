package com.n8.intouch.data

import com.n8.intouch.model.Event

interface EventsDataManager {
    fun getEvents(function:(List<Event>) -> Unit)
}