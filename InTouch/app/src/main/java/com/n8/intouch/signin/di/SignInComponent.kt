package com.n8.intouch.signin.di

import com.n8.intouch.application.ApplicationComponent
import com.n8.intouch.common.ActivityScope
import com.n8.intouch.signin.SignInActivity
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(SignInModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface SignInComponent {
    fun inject(activity: SignInActivity)
}