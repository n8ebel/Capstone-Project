package com.n8.intouch.datepicker.di

import com.n8.intouch.ApplicationComponent
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.common.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = arrayOf(DatePickerModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface DatePickerComponent {
    fun inject(fragment: DatePickerFragment)
}