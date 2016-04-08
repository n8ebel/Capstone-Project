package com.n8.intouch.addeventscreen

import android.net.Uri
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.model.Contact

class AddEventContract {

    interface ViewController {
        fun finish()

        fun showProgress()

        fun hideProgress()

        fun displayError(error:Throwable)

        fun displayContactInfo(contact: Contact)

        fun setHeaderText(text:String?)

        fun showSwipeableFragment(fragment:SwipeableFragment, tag:String, swipeable:Boolean)

        fun promptToConfirmScheduledEvent(title:String, message:String)
    }

    interface UserInteractionListener {
        fun start()

        fun stop()

        fun onContactUriReceived(contactUri: Uri)

        fun onNavIconPressed()

        fun scheduleEvent()
    }
}