package com.n8.intouch.alarm

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import com.n8.intouch.model.ScheduledEvent

/**
 * [EventScheduler] implementation that uses [JobScheduler]
 */
class JobSchedulerEventScheduler(private val mContext:Context) : EventScheduler {

    override fun scheduleEvent(event: ScheduledEvent) {
        val componentName = ComponentName(mContext, ScheduledEventJobService::class.java)
        val jobInfo = JobInfo.Builder(event.id.hashCode(), componentName)
                .setExtras(PersistableBundle(1).apply {
                    putString(ScheduledEventJobService.JOB_EXTRA_SCHEDULED_EVENT_ID, event.id)
                })
                .setPeriodic(30000)
                .build()

        with(mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler){
            schedule(jobInfo)
        }
    }

    override fun cancelScheduledEvent(event: ScheduledEvent) {
        with(mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler){
            cancel(event.id.hashCode())
        }
    }
}