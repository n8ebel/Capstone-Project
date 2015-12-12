package com.n8.intouch

import android.support.v7.widget.Toolbar

class Extensions {
    fun Toolbar.setupBackNavigation(function: () -> Unit) {
        navigationContentDescription = context.resources.getString(R.string.content_description_back_navigation)
        setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        setNavigationOnClickListener {
            run(function)
        }
    }
}