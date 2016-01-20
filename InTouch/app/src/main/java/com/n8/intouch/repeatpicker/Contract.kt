package com.n8.intouch.repeatpicker;

class Contract {
    interface View {
        fun setTimeText(text:String)

        fun setContinueButtonVisible(visible:Boolean)
    }

    interface UserInteractionListener {
        fun onViewCreated()

        fun onDateSelectorClicked()

        fun onContinueClicked()
    }
}