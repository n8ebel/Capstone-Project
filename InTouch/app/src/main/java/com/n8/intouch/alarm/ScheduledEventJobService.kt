package com.n8.intouch.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.support.v7.app.NotificationCompat
import android.telephony.SmsManager
import android.widget.Toast
import com.n8.intouch.R
import com.n8.intouch.getComponent
import com.n8.intouch.model.ScheduledEvent
import com.n8.intouch.signin.SignInActivity
import java.util.concurrent.CountDownLatch

class ScheduledEventJobService : JobService() {

    companion object {
        val JOB_EXTRA_SCHEDULED_EVENT_ID = "scheduled_event_id"
        val NOTIFICATION_ID = 1
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
            sendText(event)
        } else {
            showNotification(event)
        }

    }

    private fun sendText(event: ScheduledEvent) {
        SmsManager.getDefault().apply {
            sendTextMessage(event.phoneNumber, null, event.scheduledMessage, null, null)
        }
    }

    private fun showNotification(event: ScheduledEvent) {
        val title = baseContext.getString(R.string.notification_title)
        val message = baseContext.getString(R.string.notification_message_format, event.phoneNumber)
        val actionLabel = baseContext.getString(R.string.notification_action_label)

        val smsUri = Uri.parse("smsto:" + event.phoneNumber)
        val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", event.scheduledMessage)
        }
        val actionIntent = PendingIntent.getActivity(baseContext, 1, smsIntent, 0)

        val notification = NotificationCompat.Builder(baseContext).apply {
            setCategory(Notification.CATEGORY_PROMO)
            setContentTitle(title)
            setContentText(message)
            setSmallIcon(R.mipmap.ic_launcher)
            setAutoCancel(true)
            addAction(android.R.drawable.ic_menu_send, actionLabel, actionIntent)
            setContentIntent(actionIntent)
            setPriority(Notification.PRIORITY_HIGH)
        }.build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    // endregion Private methods
}