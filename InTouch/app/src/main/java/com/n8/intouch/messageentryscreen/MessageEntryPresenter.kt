package com.n8.intouch.messageentryscreen

class MessageEntryPresenter(var mPhoneNumber:String, val viewListener: Contract.ViewListener, val fragmentListener: MessageEntryFragment.Listener) : Contract.UserInteractionListener {

    var mMessage = ""

    override fun start() {
        viewListener.setPhoneNumber(mPhoneNumber)
    }

    override fun stop() {

    }

    override fun onMessageTextChanged(text: String) {
        mMessage = text
        updateContinueButtonVisibility()
    }

    override fun onPhoneNumberTextChanged(text: String) {
        mPhoneNumber = text
        updateContinueButtonVisibility()
    }

    override fun onContinueClicked() {
        fragmentListener.onMessageEntered(mPhoneNumber, mMessage)
    }

    // endregion Implements Contract.UserInteractionListener

    // region Private Methods

    private fun updateContinueButtonVisibility() {
        if (mMessage.length > 0 && mPhoneNumber.length > 0) viewListener.setContinueButtonVisible(true) else viewListener.setContinueButtonVisible(false)
    }

    // endregion Private Methods

}