package com.n8.intouch.browsescreen

import android.content.Intent
import android.view.View
import com.n8.intouch.model.ScheduledEvent

class BrowseContract {
    interface ViewController {
        fun displayEvents(events:List<ScheduledEvent>)

        fun displayError(error:Throwable)

        fun promptToRemoveEvent(event:ScheduledEvent)

        fun showListItemOverflowMenu()
    }

    interface UserInteractionListener {
        fun start()

        fun stop()

        fun onAddPressed()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun onListItemOverflowClicked(view: View)

        fun onRemoveEventClicked(event:ScheduledEvent)

        fun onListItemClicked(event:ScheduledEvent)
    }
}