package com.n8.intouch.common

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by n8 on 11/30/15.
 */
abstract class ComponentActivity<T : BaseComponent> :AppCompatActivity() {
    lateinit var component:T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = createComponent()
    }

    abstract fun createComponent(): T
}