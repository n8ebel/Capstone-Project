package com.n8.intouch.addeventscreen

import android.net.Uri

/**
 * Created by n8 on 11/30/15.
 */
class AddEventPresenterImpl(val view: AddEventView, val interactor:AddEventInteractor) : AddEventPresenter {
    override fun onContactUriReceived(contactUri: Uri) {
        view.showProgress()

        interactor.loadContact(contactUri, { contact ->
            view.hideProgress()

            view.displayContactInfo(contact)
        })
    }
}