package com.n8.intouch.addeventscreen

import com.n8.intouch.ApplicationComponent
import com.n8.intouch.common.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(AddModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface AddComponent {
    fun inject(fragment:AddEventFragment)
}