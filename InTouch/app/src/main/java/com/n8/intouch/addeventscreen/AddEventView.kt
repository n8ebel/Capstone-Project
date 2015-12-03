package com.n8.intouch.addeventscreen

/**
 * Created by n8 on 11/30/15.
 */
interface AddEventView {
    fun showProgress()

    fun hideProgress()

    fun displayError(error:Throwable)

    fun displayContactInfo(contactInfo:String)
}