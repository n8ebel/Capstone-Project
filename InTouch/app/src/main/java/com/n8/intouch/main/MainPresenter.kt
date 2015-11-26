package com.n8.intouch.main

import android.content.ContentResolver

/**
 * Created by n8 on 11/21/15.
 */
interface MainPresenter {
    fun showFirebaseToast()

    fun showData(arg1:String, arg2:Array<String>)

    fun handleError(exception: Exception)

    fun scheduleText(phoneNumber:String)
}