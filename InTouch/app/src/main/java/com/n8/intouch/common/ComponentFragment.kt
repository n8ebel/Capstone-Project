package com.n8.intouch.common

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by n8 on 11/30/15.
 */
class ComponentFragment<T : BaseComponent> : Fragment() {

    lateinit var component:T

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var componentActivity = activity as ComponentActivity<T>

        component = componentActivity.component
    }
}