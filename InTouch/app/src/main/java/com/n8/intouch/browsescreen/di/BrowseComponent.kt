package com.n8.intouch.browsescreen.di

import com.n8.intouch.application.ApplicationComponent
import com.n8.intouch.browsescreen.BrowseFragment
import com.n8.intouch.common.ActivityScope
import com.n8.intouch.common.BaseComponent
import dagger.Component


@ActivityScope
@Component(modules = arrayOf(BrowseModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface BrowseComponent : BaseComponent {
    fun inject(fragment: BrowseFragment)
}