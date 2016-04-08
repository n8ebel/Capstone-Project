package com.n8.intouch.repeatpicker;

class Contract {
    interface View {
        fun setTimeText(text:String)

        fun setContinueButtonVisible(visible:Boolean)
    }

    interface UserInteractionListener {

        companion object {
            val DEFAULT_FREQUENCY = "1"
        }

        fun onViewCreated()

        fun onDateSelectorClicked()

        fun onContinueClicked()

        fun onFrequencySelected(frequency: Int)

        fun onIntervalSelected(interval: Long)
    }
}