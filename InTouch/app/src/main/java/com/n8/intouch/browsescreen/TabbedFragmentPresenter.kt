package com.n8.intouch.browsescreen

import android.content.Intent

/**
 * Created by n8 on 12/2/15.
 */
interface TabbedFragmentPresenter {
    fun onAddPressed()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}