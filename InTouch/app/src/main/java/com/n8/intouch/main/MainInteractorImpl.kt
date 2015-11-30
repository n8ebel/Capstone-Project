package com.n8.intouch.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.n8.intouch.alarm.CustomAlarmReceiver
import com.n8.intouch.contentprovider.ProviderContract
import java.util.*

class MainInteractorImpl(val context: Context, val firebase: Firebase) : MainInteractor, ValueEventListener {
    override fun onDataChange(p0: DataSnapshot?) {
        throw UnsupportedOperationException()
    }

    override fun onCancelled(p0: FirebaseError?) {
        throw UnsupportedOperationException()
    }

    override fun handleClick(body: (result:String) -> Unit) {

        firebase.child("goo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                var data: String = snapshot?.getValue(String::class.java) ?: "error"

                return body(data)
            }

            override fun onCancelled(p0: FirebaseError?) {
                return body("there was an error")
            }

        })

        firebase.child("goo").addValueEventListener(this)
    }

    override fun getData(arg1: String, arg2: Array<String>) {
        context.contentResolver.query(ProviderContract.BASE_CONTENT_URI, null, arg1, arg2, null)
    }

    override fun scheduleText(phoneNumber: String) {
        var cal = Calendar.getInstance(); //retrieves a calendar object w/ current time
        cal.add(Calendar.MINUTE, 1); //adds 1 minutes to current time

        var alarmIntent = Intent(context, CustomAlarmReceiver::class.java)

        //pass extra data to CustomAlarmReceiver intent to be handled when the alarm goes off
        alarmIntent.putExtra("alarm_message", "Hello World");
        alarmIntent.putExtra("number", phoneNumber);

        // creates a new PendingIntent using the static variable eventID.
        //  using eventID allows you to create multiple events with the same code
        //  without a unique id the intent would just be updated with new extras each time its created
        //
        var pendingAlarm = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //       eventID+=1;

        // get the Alarm service and set the alarm with the time to go off and what to do when the
        // alarm is received
        var am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingAlarm);
    }
}