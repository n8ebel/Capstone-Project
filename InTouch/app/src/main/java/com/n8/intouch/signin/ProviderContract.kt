package com.n8.intouch.signin

import android.net.Uri

/**
 * Defines contract through which the ContentProvider can be used
 */
object ProviderContract {
    val CONTENT_AUTHORITY = "com.n8.intouch.provider"

    val USER_NAME = "user_name"

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    fun buildCurrentUsernameUri() : Uri {
        return BASE_CONTENT_URI.buildUpon()
                .appendPath(USER_NAME)
                .build()
    }
}
