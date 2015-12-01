package com.n8.intouch.addeventscreen

import com.n8.intouch.ApplicationComponent
import com.n8.intouch.common.BaseComponent
import com.n8.intouch.common.FragmentScope
import dagger.Component

/**
 * Created by n8 on 11/30/15.
 */
@FragmentScope
@Component(modules = arrayOf(AddEventModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface AddEventComponent : BaseComponent {
    fun inject(fragment: AddEventFragment)
}