package com.n8.intouch.messageentryscreen.di

import com.n8.intouch.common.FragmentScope
import com.n8.intouch.messageentryscreen.MessageEntryFragment
import dagger.Component

@FragmentScope
@Component(modules = arrayOf(MessageEntryModule::class))
interface MessageEntryComponent {
    fun inject(fragment: MessageEntryFragment)
}