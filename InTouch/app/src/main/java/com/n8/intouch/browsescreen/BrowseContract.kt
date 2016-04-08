package com.n8.intouch.browsescreen

import android.content.Intent
import com.n8.intouch.model.ScheduledEvent

class BrowseContract {
    interface ViewController {
        fun setUsernameText(username:String)

        fun displayEvents(events:List<ScheduledEvent>)

        fun displayError(error:Throwable)
    }

    interface UserInteractionListener {
        fun start()

        fun stop()

        fun onAddPressed()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}