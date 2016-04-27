package com.n8.intouch.analytics

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.n8.intouch.R

class GoogleAnalyticsTracker(private val mContext: Context) : AnalyticsTracker {

    private val mTracker: Tracker

    init {
        mTracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker)
    }

    // region Implements AnalyticsTracker

    override fun trackEvent(category: String, action: String) {
        val params = HitBuilders.EventBuilder().setCategory(category).setAction(action).build()
        mTracker.send(params)
    }

    override fun trackScreen(screenName: String) {
        mTracker.setScreenName(screenName)
        mTracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    // endregion Implements AnalyticsTracker
}