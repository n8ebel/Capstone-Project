package com.n8.intouch.analytics

interface AnalyticsTracker {
    companion object {
        val CATEGORY_USER_ACTION = "user_action"
    }

    fun trackEvent(category:String, action:String)
    fun trackScreen(screenName:String)
}