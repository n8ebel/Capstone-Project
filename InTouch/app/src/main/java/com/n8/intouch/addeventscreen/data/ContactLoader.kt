package com.n8.intouch.addeventscreen.data

import android.graphics.Bitmap
import android.net.Uri
import com.n8.intouch.model.Contact

/**
 * Created by n8 on 11/30/15.
 */
interface ContactLoader {
    interface ContactLoadedListener{
        fun onContactLoaded(contact: Contact)
    }

    fun loadContact(contactUri: Uri, listener: (Contact) -> Unit)
}