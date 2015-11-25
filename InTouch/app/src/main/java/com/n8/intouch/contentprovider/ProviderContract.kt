package com.n8.intouch.contentprovider

import android.net.Uri

/**
 * Created by n8 on 11/25/15.
 */
object ProviderContract {
    val CONTENT_AUTHORITY = "com.n8.intouch.provider"

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    fun buildTestUri(foo: String): Uri {
        return BASE_CONTENT_URI
    }
}
