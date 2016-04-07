package com.n8.intouch.browsescreen

import android.content.Intent

/**
 * Created by n8 on 12/4/15.
 */
class BrowseContract {
    interface ViewController {
        fun displayError(error:Throwable)
    }

    interface UserInteractionListener {
        fun onAddPressed()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}