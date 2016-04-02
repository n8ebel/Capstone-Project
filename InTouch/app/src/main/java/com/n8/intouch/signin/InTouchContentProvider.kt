package com.n8.intouch.signin

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.n8.intouch.application.InTouchApplication
import java.util.*

class InTouchContentProvider : ContentProvider() {

    companion object sMatcher {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        val USERNAME = 0

        init{
            matcher.addURI(ProviderContract.CONTENT_AUTHORITY, ProviderContract.USER_NAME, USERNAME)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        when (sMatcher.matcher.match(uri)) {
            USERNAME -> return ProviderContract.USER_NAME
        }

        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (getType(uri) != ProviderContract.USER_NAME) {
            return null
        }

        InTouchApplication.graph.getSharedPreferences()
                .edit()
                .putString(
                        ProviderContract.USER_NAME,
                        values!!.getAsString(ProviderContract.USER_NAME))
                .apply()

        return ProviderContract.BASE_CONTENT_URI.buildUpon().appendPath(ProviderContract.USER_NAME).build()
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        if (getType(uri) != ProviderContract.USER_NAME) {
            return null
        }

        val username = InTouchApplication.graph.getSharedPreferences().getString(ProviderContract.USER_NAME, null)

        val cursor = MatrixCursor(arrayOf("username"))

        if (username != null) {
            cursor.addRow(arrayOf(username))
        }

        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {

        if (getType(uri) != ProviderContract.USER_NAME) {
            return -1
        }

        return 0
    }
}
