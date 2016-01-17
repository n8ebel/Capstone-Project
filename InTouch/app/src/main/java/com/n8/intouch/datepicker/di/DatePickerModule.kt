package com.n8.intouch.datepicker.di

import android.content.Context
import android.net.Uri
import com.n8.intouch.addeventscreen.AddEventContract
import com.n8.intouch.addeventscreen.AddEventPresenter
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.data.ContentProviderContactLoader
import com.n8.intouch.datepicker.Contract
import com.n8.intouch.datepicker.DatePickerPresenter
import com.n8.intouch.model.Contact
import dagger.Module
import dagger.Provides

@Module
class DatePickerModule(val contact: Contact, val view: Contract.View) {

//    @Provides
//    fun provideContact() : Contact {
//        return contact
//    }
//
//    @Provides
//    fun providePresenter() : Contract.UserInteractionListener {
//        return DatePickerPresenter(view)
//    }
}
