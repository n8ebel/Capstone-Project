package com.n8.intouch.datepicker

import com.n8.intouch.model.Contact

class DatePickerPresenter(val view:Contract.View, val listener:DatePickerFragment.Listener) : Contract.UserInteractionListener {

    var selectedDate = -1L

    override fun onContactReceived(contact: Contact) {
        view.setContinueButtonVisible(false)
        view.bindEvents(contact.events)
    }

    override fun onDateSelected(timestamp: Long) {
        selectedDate = timestamp
        view.setContinueButtonVisible(true)
    }

    override fun onContinueClicked() {
        listener.onDateSelected(selectedDate)
    }
}