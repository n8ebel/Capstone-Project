package com.n8.intouch.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.NonNull
import com.n8.intouch.model.ScheduledEvent
import java.util.*

class AlarmManagerEventScheduler(private val mContext: Context,
                                 private val mSharedPreferences: SharedPreferences,
                                 private val mAlarmManager:AlarmManager) : EventScheduler {

    companion object {
        val PREFS_KEY_NEXT_ID = "next_id"
        val INVALID_ID = -1
    }

    // region Implements EventScheduler

    override fun scheduleEvent(event: ScheduledEvent) {
        val pendingIntent = createPendingIntent(event)

        mAlarmManager.apply {
            setRepeating(AlarmManager.RTC_WAKEUP,
                    getTimeUntilFirstAlarm(event),
                    10000,//event.repeatDuration,
                   pendingIntent )
        }
    }

    override fun cancelScheduledEvent(event: ScheduledEvent) {
        mAlarmManager.cancel(createPendingIntent(event))
    }

    // endregion Implements EventScheduler

    // region Private Methods

    private fun createPendingIntent(event:ScheduledEvent): PendingIntent {
        return PendingIntent.getBroadcast(mContext,
                saveId(event),
                ScheduledEventReceiver.createIntent(mContext, event), 0)
    }

    private fun getTimeUntilFirstAlarm(@NonNull event: ScheduledEvent) : Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(event.startDateTimestamp)
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                event.startDateHour,
                event.startDateMin)

        return 5000//calendar.timeInMillis - System.currentTimeMillis()
    }

    private fun getNextId() : Int {
        var id = mSharedPreferences.getInt(PREFS_KEY_NEXT_ID, 0)

        mSharedPreferences.edit().putInt(PREFS_KEY_NEXT_ID, id+1).apply()

        return id
    }

    private fun saveId(@NonNull event: ScheduledEvent) : Int {
        val id = getNextId()

        mSharedPreferences.edit().putInt(event.id, id).apply()

        return id
    }

    private fun getId(@NonNull event: ScheduledEvent) : Int {
        return mSharedPreferences.getInt(event.id, INVALID_ID)
    }

    private fun removeId(@NonNull event: ScheduledEvent) {
        mSharedPreferences.edit().remove(event.id).apply()
    }

    // endregion Private Methods
}