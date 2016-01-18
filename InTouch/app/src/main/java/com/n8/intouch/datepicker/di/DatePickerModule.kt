package com.n8.intouch.datepicker.di

import com.n8.intouch.datepicker.Contract
import com.n8.intouch.datepicker.DatePickerPresenter
import com.n8.intouch.model.Contact
import dagger.Module
import dagger.Provides

@Module
class DatePickerModule(val contact: Contact, val view: Contract.View) {

    @Provides
    fun providesContact() : Contact {
        return contact
    }

    @Provides
    fun providesUserInteractionListener() : Contract.UserInteractionListener {
        return DatePickerPresenter(view)
    }

    @Provides
    fun providesGoo(): Int {
        return 5
    }
}
