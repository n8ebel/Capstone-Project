package com.n8.intouch.messageentryscreen

class Contract {
    interface ViewListener {
        fun setContinueButtonVisible(visible:Boolean)

        fun setPhoneNumber(number:String)
    }

    interface UserInteractionListener {
        fun start()

        fun stop()

        fun onMessageTextChanged(text:String)

        fun onPhoneNumberTextChanged(text:String)

        fun onContinueClicked(test:String)
    }
}
