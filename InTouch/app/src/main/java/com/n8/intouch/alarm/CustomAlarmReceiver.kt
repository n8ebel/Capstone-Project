package com.n8.intouch.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast

class CustomAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            sendSms(intent)
        }
        catch (e:Exception) {
            e.printStackTrace();
        }
    }

    fun sendSms(intent: Intent) {
        var bundle = intent.getExtras();
        var smsManager = SmsManager.getDefault();

        var smsText = bundle.getString("alarm_message");
        var smsNumber = bundle.getString("number");

        smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
    }
}
