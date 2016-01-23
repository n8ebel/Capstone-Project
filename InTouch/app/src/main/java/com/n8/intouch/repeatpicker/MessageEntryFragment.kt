package com.n8.intouch.repeatpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.messageentryscreen.di.MessageEntryComponent

class MessageEntryFragment : SwipeableFragment() {

    var component: MessageEntryComponent? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater!!.inflate(R.layout.fragment_message_entry, container, false)

        return rootView
    }
}