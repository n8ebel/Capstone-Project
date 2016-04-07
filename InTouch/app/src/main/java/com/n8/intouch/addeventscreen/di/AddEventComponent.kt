package com.n8.intouch.addeventscreen.di

import com.n8.intouch.application.ApplicationComponent
import com.n8.intouch.addeventscreen.AddEventFragment
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.common.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(AddEventModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface AddEventComponent {
    fun inject(fragment: AddEventFragment)
}