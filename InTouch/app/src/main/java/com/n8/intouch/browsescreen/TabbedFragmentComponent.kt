package com.n8.intouch.browsescreen

import com.n8.intouch.ApplicationComponent
import com.n8.intouch.common.ActivityScope
import com.n8.intouch.common.BaseComponent
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(TabbedFragmentModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface TabbedFragmentComponent : BaseComponent {
    fun inject(fragment: BrowseFragment)
}