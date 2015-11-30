package com.n8.intouch.main

import com.n8.intouch.common.ActivityScope
import com.n8.intouch.ApplicationComponent
import com.n8.intouch.common.BaseComponent
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface MainComponent : BaseComponent {
    fun inject(mainActivity: MainActivity)
}