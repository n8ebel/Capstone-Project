package com.n8.intouch.contentprovider

import android.net.Uri

/**
 * Created by n8 on 11/25/15.
 */
object ProviderContract {
    val CONTENT_AUTHORITY = "com.n8.intouch.provider"

    val START_TIME = "start_time"
    val START_HOUR = "start_hour"
    val START_MIN = "start_min"
    val INTERVAL = "interval"
    val DURATION = "duration"
    val MESSAGE = "message"

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    fun buildTestUri(foo: String): Uri {
        return BASE_CONTENT_URI
    }

    fun buildAddScheduledMessageUri(startTimestamp:Long, hour:Int, min:Int, interval:Int, duration:Long, msg:String): Uri {
        return BASE_CONTENT_URI.buildUpon()
                .appendQueryParameter(START_TIME, startTimestamp.toString())
                .appendQueryParameter(START_HOUR, hour.toString())
                .appendQueryParameter(START_MIN, min.toString())
                .appendQueryParameter(INTERVAL, interval.toString())
                .appendQueryParameter(DURATION, duration.toString())
                .appendQueryParameter(MESSAGE, msg)
                .build()
    }
}
