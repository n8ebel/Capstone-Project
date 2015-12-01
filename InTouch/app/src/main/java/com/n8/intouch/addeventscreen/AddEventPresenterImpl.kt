package com.n8.intouch.addeventscreen

/**
 * Created by n8 on 11/30/15.
 */
class AddEventPresenterImpl(var view: AddEventView) : AddEventPresenter {
    override fun onContactDataLoaded(contactData: String) {
        view.displayContactInfo(contactData)
    }

}