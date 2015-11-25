package com.n8.intouch.main

import android.content.ContentResolver

/**
 * Created by n8 on 11/21/15.
 */
interface MainInteractor {
    fun handleClick( body : (result:String) -> Unit)

    fun getData(contentResolver: ContentResolver, arg1:String, arg2:Array<String>)
}