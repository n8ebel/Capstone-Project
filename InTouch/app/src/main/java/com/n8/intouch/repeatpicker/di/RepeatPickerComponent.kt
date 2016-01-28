package com.n8.intouch.repeatpicker.di

import com.n8.intouch.common.FragmentScope
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import dagger.Component

@FragmentScope
@Component(modules = arrayOf(RepeatPickerModule::class))
interface RepeatPickerComponent {
    fun inject(fragment: RepeatPickerFragment)
}