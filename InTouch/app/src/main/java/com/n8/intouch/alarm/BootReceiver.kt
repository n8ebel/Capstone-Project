package com.n8.intouch.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.n8.intouch.application.InTouchApplication
import com.n8.intouch.model.ScheduledEvent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val eventScheduler = InTouchApplication.component.getEventScheduler()
            val dataManager = InTouchApplication.component.getDataManager()

            dataManager.refreshEvents { events ->
                events.forEach {
                    eventScheduler.scheduleEvent(it)
                }
            }

            //eventScheduler.scheduleEvent(ScheduledEvent("goobey_id"))
        }
    }
}