package com.n8.intouch.addeventscreen

import android.net.Uri
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.model.User

/**
 * Created by n8 on 11/30/15.
 */
class AddEventPresenter(val viewController: AddEventContract.ViewController,
                        val interactor: ContactLoader,
                        val currentUser: User,
                        val eventManager: EventsDataManager) : AddEventContract.UserInteractionListener {

    override fun start() {
        eventManager.getEvents {

        }
    }

    override fun stop() {

    }

    override fun onContactUriReceived(contactUri: Uri) {
        viewController.showProgress()

        interactor.loadContact(contactUri, { contact ->
            viewController.hideProgress()

            viewController.displayContactInfo(contact)
        })
    }

    override fun onNavIconPressed() {
        viewController.finish()
    }
}