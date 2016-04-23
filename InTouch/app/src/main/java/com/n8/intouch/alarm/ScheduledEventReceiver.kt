package com.n8.intouch.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.NonNull
import android.support.v7.app.NotificationCompat
import android.telephony.SmsManager
import android.util.Log
import com.n8.intouch.R
import com.n8.intouch.application.InTouchApplication
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.getComponent
import com.n8.intouch.model.ScheduledEvent

class ScheduledEventReceiver : BroadcastReceiver(){

    companion object {
        val TAG = "ScheduledEventReceiver"
        val EXTRA_EVENT_ID = "event_id"
        val NOTIFICATION_ID = 1

        fun createIntent(@NonNull context: Context, @NonNull event: ScheduledEvent) : Intent {
            return Intent(context, ScheduledEventReceiver::class.java).apply {
                putExtra(EXTRA_EVENT_ID, event.id)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")

        if (context == null) {
            return
        }

        intent?.getStringExtra(EXTRA_EVENT_ID)?.apply {
            Log.d(TAG, "getting event with id $this")
            InTouchApplication.component.getDataManager().getEvent(this, { event, error ->
                handleScheduledEvent(context, event)
            })
        }
    }

    // region Private methods

    private fun handleScheduledEvent(context:Context, event:ScheduledEvent?) {
        if(event == null) return

        val autoSendText = InTouchApplication.component.getSharedPreferences().getBoolean(context.getString(R.string.settings_key_auto_send), false)

        if (autoSendText) {
            sendText(event)
        } else {
            showNotification(context, event)
        }
    }

    private fun sendText(event: ScheduledEvent) {
        SmsManager.getDefault().apply {
            sendTextMessage(event.phoneNumber, null, event.scheduledMessage, null, null)
        }
    }

    private fun showNotification(context:Context, event: ScheduledEvent) {
        val title = context.getString(R.string.notification_title)
        val message = context.getString(R.string.notification_message_format, event.phoneNumber)
        val actionLabel = context.getString(R.string.notification_action_label)

        val smsUri = Uri.parse("smsto:" + event.phoneNumber)
        val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", event.scheduledMessage)
        }
        val actionIntent = PendingIntent.getActivity(context, 1, smsIntent, 0)

        val notification = NotificationCompat.Builder(context).apply {
            setCategory(Notification.CATEGORY_PROMO)
            setContentTitle(title)
            setContentText(message)
            setSmallIcon(R.mipmap.ic_launcher)
            setAutoCancel(true)
            addAction(android.R.drawable.ic_menu_send, actionLabel, actionIntent)
            setContentIntent(actionIntent)
            setPriority(Notification.PRIORITY_HIGH)
        }.build()

        val notificationManager = context.getSystemService(JobService.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    // endregion Private methods
}