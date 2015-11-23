package com.n8.intouch.main

/**
 * Created by n8 on 11/21/15.
 */
interface MainView {
    fun showProgress()

    fun hideProgress()

    fun showResult(result:String)
}