package com.n8.intouch.datepicker.di

import com.n8.intouch.datepicker.Contract
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.datepicker.DatePickerPresenter
import com.n8.intouch.model.Contact
import dagger.Module
import dagger.Provides

@Module
class DatePickerModule(val contact: Contact, val view: Contract.View, val listener:DatePickerFragment.Listener) {

    @Provides
    fun providessContact() : Contact {
        return contact
    }

    @Provides
    fun providessUserInteractionListener() : Contract.UserInteractionListener {
        return DatePickerPresenter(view, listener)
    }

    @Provides
    fun providesGoo(): Int {
        return 5
    }
}
