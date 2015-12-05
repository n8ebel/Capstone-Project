package com.n8.intouch.addeventscreen

import android.net.Uri
import com.n8.intouch.addeventscreen.data.ContactLoader

/**
 * Created by n8 on 11/30/15.
 */
class AddEventPresenter(val view: AddEventContract.View, val interactor: ContactLoader) : AddEventContract.UserInteractionListener {
    override fun onContactUriReceived(contactUri: Uri) {
        view.showProgress()

        interactor.loadContact(contactUri, { contact ->
            view.hideProgress()

            view.displayContactInfo(contact)
        })
    }
}