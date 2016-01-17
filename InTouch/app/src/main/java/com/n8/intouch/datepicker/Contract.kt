package com.n8.intouch.datepicker

class Contract {
    interface View {

    }

    interface UserInteractionListener {
        fun onDateSelected(timestamp:Long)

        fun onContinueClicked()
    }
}
