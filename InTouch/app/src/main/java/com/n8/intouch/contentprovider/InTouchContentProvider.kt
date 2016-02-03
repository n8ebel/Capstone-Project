package com.n8.intouch.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.n8.intouch.application.InTouchApplication
import java.util.*

class InTouchContentProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val start_timestamp = uri.getQueryParameter(ProviderContract.START_TIME)
        val start_hour = uri.getQueryParameter(ProviderContract.START_HOUR)
        val start_min = uri.getQueryParameter(ProviderContract.START_MIN)
        val interval = uri.getQueryParameter(ProviderContract.INTERVAL)
        val duration = uri.getQueryParameter(ProviderContract.DURATION)
        val message = uri.getQueryParameter(ProviderContract.MESSAGE)

        val newEventRef = InTouchApplication.graph.getFirebase().child("events").push()

        val newEvent = hashMapOf(
                Pair(ProviderContract.START_TIME, start_timestamp),
                Pair(ProviderContract.START_HOUR, start_hour),
                Pair(ProviderContract.START_MIN, start_min),
                Pair(ProviderContract.INTERVAL, interval),
                Pair(ProviderContract.DURATION, duration),
                Pair(ProviderContract.MESSAGE, message)
        )
        newEventRef.setValue(newEvent)

        return null
    }

    override fun onCreate(): Boolean {
        // TODO: Implement this to initialize your content provider on startup.
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // TODO: Implement this to handle query requests from clients.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        // TODO: Implement this to handle requests to update one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }
}
