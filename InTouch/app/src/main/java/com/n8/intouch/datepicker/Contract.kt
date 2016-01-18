package com.n8.intouch.datepicker

import android.app.usage.UsageEvents
import com.n8.intouch.model.Contact
import com.n8.intouch.model.Event

class Contract {
    interface View {
        fun bindEvents(events:List<Event>)

        fun setContinueButtonVisible(visible:Boolean)
    }

    interface UserInteractionListener {
        fun onContactReceived(contact: Contact)

        fun onDateSelected(timestamp:Long)

        fun onContinueClicked()
    }
}
