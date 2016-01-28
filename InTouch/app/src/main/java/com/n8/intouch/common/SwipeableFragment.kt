package com.n8.intouch.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open class SwipeableFragment : Fragment() {

    var touchListener: View.OnTouchListener? = null

    override fun onResume() {
        super.onResume()
        view.setOnTouchListener { view, motionEvent ->
            touchListener?.onTouch(view, motionEvent)

            false
        }
    }

    override fun onPause() {
        super.onPause()
        view.setOnTouchListener { view, motionEvent ->
            // do nothing
            false
        }
    }
}
