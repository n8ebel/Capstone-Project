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
            Log.d("BOOTReceiver", "getting events")
            dataManager.refreshEvents { events ->
                Log.d("BootReceiver", "there were")
                events.forEach {
                    Log.d("BootReceiver", "scheduling event for id ${it.id}")
                    eventScheduler.scheduleEvent(it)
                }
            }

            //eventScheduler.scheduleEvent(ScheduledEvent("goobey_id"))
        }
    }
}