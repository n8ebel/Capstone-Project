package com.n8.intouch.datepicker

import com.n8.intouch.model.Contact

class DatePickerPresenter(val view:Contract.View, val listener:DatePickerFragment.Listener) : Contract.UserInteractionListener {
    override fun onContactReceived(contact: Contact) {
        view.setContinueButtonVisible(false)
        view.bindEvents(contact.events)
    }

    override fun onDateSelected(timestamp: Long) {
        view.setContinueButtonVisible(true)
        listener.onDateSelected(timestamp)
    }

    override fun onContinueClicked() {
        listener.onContinueClicked()
    }
}