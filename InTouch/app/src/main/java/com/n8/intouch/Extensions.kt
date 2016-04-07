package com.n8.intouch

import android.app.Activity
import android.app.Application
import android.support.v7.widget.Toolbar
import com.n8.intouch.application.ApplicationComponent
import com.n8.intouch.application.InTouchApplication

fun Toolbar.setupBackNavigation(function: () -> Unit) {
    navigationContentDescription = context.resources.getString(R.string.content_description_back_navigation)
    setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
    setNavigationOnClickListener {
        run(function)
    }
}

fun Application.getCurrentActivity() : Activity {
    return (this as InTouchApplication).getCurrentActivity()
}

fun Application.setCurrentActivity(activity:Activity) {
    (this as InTouchApplication).activity = activity
}

fun Application.getComponent() : ApplicationComponent {
    return InTouchApplication.component
}