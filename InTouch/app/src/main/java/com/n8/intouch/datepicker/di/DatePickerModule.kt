package com.n8.intouch.datepicker.di

import com.n8.intouch.datepicker.Contract
import com.n8.intouch.datepicker.DatePickerPresenter
import com.n8.intouch.model.Contact
import dagger.Module
import dagger.Provides

@Module
class DatePickerModule(val contact: Contact, val view: Contract.View) {

    @Provides
    fun provideContact() : Contact {
        return contact
    }

    @Provides
    fun providePresenter() : Contract.UserInteractionListener {
        return DatePickerPresenter(view)
    }
}
