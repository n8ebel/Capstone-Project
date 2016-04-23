package com.n8.intouch.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R

class SettingsFragment : PreferenceFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addPreferencesFromResource(R.xml.app_preferences)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

}