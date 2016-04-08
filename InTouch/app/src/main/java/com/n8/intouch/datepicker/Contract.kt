package com.n8.intouch.datepicker

import com.n8.intouch.model.Contact
import com.n8.intouch.model.SystemEvent

class Contract {
    interface View {
        fun bindEvents(events:List<SystemEvent>)

        fun setContinueButtonVisible(visible:Boolean)
    }

    interface UserInteractionListener {
        fun onContactReceived(contact: Contact)

        fun onDateSelected(timestamp:Long)

        fun onContinueClicked()
    }
}
