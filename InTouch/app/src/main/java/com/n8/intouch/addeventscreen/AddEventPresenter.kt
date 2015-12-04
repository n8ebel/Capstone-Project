package com.n8.intouch.addeventscreen

import android.net.Uri

/**
 * Created by n8 on 11/30/15.
 */
interface AddEventPresenter {
    fun onContactUriReceived(contactUri: Uri)
}