package com.n8.intouch.datepicker

import com.n8.intouch.model.Contact

class DatePickerPresenter(val view:Contract.View, val listener:DateSelectionListener) : Contract.UserInteractionListener {
    override fun onContactReceived(contact: Contact) {
        view.bindEvents(contact.events)
    }

    override fun onDateSelected(timestamp: Long) {
        listener.onDateSelected(timestamp)
    }

}