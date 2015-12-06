package com.n8.intouch.addeventscreen.data

import android.net.Uri

/**
 * Created by n8 on 11/30/15.
 */
interface ContactLoader {
    interface ContactLoadedListener{
        fun onContactLoaded(contact: Contact)
    }

    data class Contact(val name:String)

    fun loadContact(contactUri: Uri, listener: (Contact) -> Unit)
}