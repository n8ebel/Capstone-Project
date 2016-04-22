package com.n8.intouch.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.n8.intouch.application.InTouchApplication

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val eventScheduler = InTouchApplication.component.getEventScheduler()
        val dataManager = InTouchApplication.component.getDataManager()

        dataManager.getEvents { events ->
            events.forEach {
                eventScheduler.scheduleEvent(it)
            }
        }
    }
}