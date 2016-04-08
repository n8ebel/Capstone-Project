package com.n8.intouch.addeventscreen.data

import android.net.Uri
import com.n8.intouch.model.Contact

interface ContactLoader {
    fun loadContact(contactUri: Uri, listener: (Contact) -> Unit)
}