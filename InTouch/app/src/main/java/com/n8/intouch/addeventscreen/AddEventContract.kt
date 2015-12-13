package com.n8.intouch.addeventscreen

import android.net.Uri
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.model.Contact

/**
 * Created by n8 on 12/4/15.
 */
class AddEventContract {
    interface View {
        fun finish()

        fun showProgress()

        fun hideProgress()

        fun displayError(error:Throwable)

        fun displayContactInfo(contact: Contact)
    }

    interface UserInteractionListener {
        fun onContactUriReceived(contactUri: Uri)

        fun onNavIconPressed()

        fun onDateSelected(timestamp:Long)
    }
}