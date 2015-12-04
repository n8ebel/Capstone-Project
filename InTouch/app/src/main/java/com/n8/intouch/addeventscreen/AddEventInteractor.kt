package com.n8.intouch.addeventscreen

import android.net.Uri

/**
 * Created by n8 on 11/30/15.
 */
interface AddEventInteractor {
    interface ContactLoadedListener{
        fun onContactLoaded(contact:Contact)
    }

    data class Contact(val name:String)

    fun loadContact(contactUri: Uri, listener: (Contact) -> Unit)
}