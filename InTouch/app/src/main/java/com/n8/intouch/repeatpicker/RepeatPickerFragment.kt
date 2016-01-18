package com.n8.intouch.repeatpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment

/**
 * Allows use to pick a repeat schedule for an event
 */
class RepeatPickerFragment : SwipeableFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater!!.inflate(R.layout.repeat_picker_card, container, false)

        return rootView
    }
}
