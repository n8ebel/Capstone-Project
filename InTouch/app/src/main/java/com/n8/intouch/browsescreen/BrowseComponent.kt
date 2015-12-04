package com.n8.intouch.browsescreen

import com.n8.intouch.ApplicationComponent
import com.n8.intouch.common.ActivityScope
import com.n8.intouch.common.BaseComponent
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(BrowseModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface BrowseComponent : BaseComponent {
    fun inject(fragment: BrowseFragment)
}