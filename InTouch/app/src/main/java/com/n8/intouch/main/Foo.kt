package com.n8.intouch.main

import android.util.Log
import javax.inject.Inject

/**
 * Created by n8 on 11/21/15.
 */
class Foo(mainComponent: MainComponent) {

    @Inject
    lateinit var goo:String

    init{
        Log.d("foo","initFoo")
        mainComponent.inject(this)
    }
}