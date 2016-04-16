package com.n8.intouch.alarm

import android.app.job.JobParameters
import android.app.job.JobService
import android.widget.Toast
import com.n8.intouch.getComponent
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
                Toast.makeText(baseContext, event?.id + event?.scheduledMessage, Toast.LENGTH_LONG).show()
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
}