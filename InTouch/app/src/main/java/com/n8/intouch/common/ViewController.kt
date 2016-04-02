package com.n8.intouch.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface ViewController {
    fun createView(inflater: LayoutInflater, parent: ViewGroup, stateBundle:Bundle?) : View

    fun saveState(stateBundle: Bundle?)

    fun showView()

    fun showView(function: () -> Unit)

    fun hideView()

    fun hideView(function: () -> Unit)
}