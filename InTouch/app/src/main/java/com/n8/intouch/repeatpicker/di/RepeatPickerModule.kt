package com.n8.intouch.repeatpicker.di

import android.content.Context
import com.n8.intouch.repeatpicker.Contract
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import com.n8.intouch.repeatpicker.RepeatPickerPresenter
import dagger.Module
import dagger.Provides

@Module
class RepeatPickerModule(val context: Context, val view: Contract.View, val listener: RepeatPickerFragment.Listener) {

    @Provides
    fun providesUserInteractionListener(): Contract.UserInteractionListener {
        return RepeatPickerPresenter(context, view, listener)
    }

    @Provides
    fun providesListener(): RepeatPickerFragment.Listener {
        return listener
    }
}