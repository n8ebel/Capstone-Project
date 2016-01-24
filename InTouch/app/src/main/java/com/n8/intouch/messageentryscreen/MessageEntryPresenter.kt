package com.n8.intouch.messageentryscreen

class MessageEntryPresenter(val viewListener: Contract.ViewListener, val fragmentListener: MessageEntryFragment.Listener) : Contract.UserInteractionListener {

    // region Implements Contract.UserInteractionListener

    override fun onTextChanged(text: String) {
        if (text.length > 0) viewListener.setContinueButtonVisible(true) else viewListener.setContinueButtonVisible(false)
    }

    override fun onContinueClicked(test: String) {
        fragmentListener.onMessageEntered(test)
    }

    // endregion Implements Contract.UserInteractionListener

}