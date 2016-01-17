package com.n8.intouch.datepicker

class DatePickerPresenter(view:Contract.View) : Contract.UserInteractionListener {

    override fun onContinueClicked() {
        throw UnsupportedOperationException()
    }

    override fun onDateSelected(timestamp: Long) {
        throw UnsupportedOperationException()
    }

}