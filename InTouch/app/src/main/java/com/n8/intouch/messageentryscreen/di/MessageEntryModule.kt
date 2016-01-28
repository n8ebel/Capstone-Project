package com.n8.intouch.messageentryscreen.di

import com.n8.intouch.messageentryscreen.Contract
import com.n8.intouch.messageentryscreen.MessageEntryFragment
import com.n8.intouch.messageentryscreen.MessageEntryPresenter
import dagger.Module
import dagger.Provides

@Module
class MessageEntryModule(val view:Contract.ViewListener, val fragmentListener: MessageEntryFragment.Listener) {

    @Provides
    fun provideUserInteractionListener() : Contract.UserInteractionListener {
        return MessageEntryPresenter(view, fragmentListener)
    }

}