package com.n8.intouch.alarm

import android.app.job.JobParameters
import android.app.job.JobService
import android.preference.PreferenceManager
import android.widget.Toast
import com.n8.intouch.R
import com.n8.intouch.getComponent
import com.n8.intouch.model.ScheduledEvent
import java.util.concurrent.CountDownLatch

class ScheduledEventJobService : JobService() {

    companion object {
        val JOB_EXTRA_SCHEDULED_EVENT_ID = "scheduled_event_id"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val eventId = params?.extras?.getString(JOB_EXTRA_SCHEDULED_EVENT_ID) ?: return false

        Thread(Runnable(){
            val latch = CountDownLatch(1)

            application.getComponent().getDataManager().getEvent(eventId, { event, error ->
                respondToJob(event)
                latch.countDown()
            })

            latch.await()

            jobFinished(params, false)

        }).start()

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    // region Private methods

    private fun respondToJob(event: ScheduledEvent?) {
        if(event == null) return

        val autoSendText = application.getComponent().getSharedPreferences().getBoolean(baseContext.getString(R.string.settings_key_auto_send), false)

        if (autoSendText) {
            Toast.makeText(baseContext, "Auto-Send", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(baseContext, "Show Notification", Toast.LENGTH_LONG).show()
        }

    }

    // endregion Private methods
}