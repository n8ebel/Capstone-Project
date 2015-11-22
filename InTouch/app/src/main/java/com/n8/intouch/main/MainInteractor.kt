package com.n8.intouch.main

/**
 * Created by n8 on 11/21/15.
 */
interface MainInteractor {
    fun handleClick( body : (result:String) -> Unit)
}