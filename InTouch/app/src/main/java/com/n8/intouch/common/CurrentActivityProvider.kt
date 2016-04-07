package com.n8.intouch.common

import android.app.Activity

interface CurrentActivityProvider {
    fun getCurrentActivity() : Activity
}