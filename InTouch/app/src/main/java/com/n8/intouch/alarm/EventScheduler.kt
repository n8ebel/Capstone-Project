package com.n8.intouch.alarm

import com.n8.intouch.model.ScheduledEvent


interface EventScheduler {
    fun scheduleEvent(event: ScheduledEvent)
    fun cancelScheduledEvent(event: ScheduledEvent)
}