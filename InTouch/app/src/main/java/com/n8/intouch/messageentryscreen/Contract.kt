package com.n8.intouch.messageentryscreen

class Contract {
    interface ViewListener {
        fun setContinueButtonVisible(visible:Boolean)
    }

    interface UserInteractionListener {
        fun onTextChanged(text:String)

        fun onContinueClicked(test:String)
    }
}
