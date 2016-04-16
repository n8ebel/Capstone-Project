package com.n8.intouch.browsescreen

import android.content.Intent
import android.view.View
import com.n8.intouch.model.ScheduledEvent

class BrowseContract {
    interface ViewController {
        fun displayEvents(events:List<ScheduledEvent>)

        fun displayAddedEvent(event: ScheduledEvent, index:Int)

        fun hideRemovedEvent(event: ScheduledEvent, index:Int)

        fun displayError(error:Throwable)

        fun displayError(message:String)

        fun promptToRemoveEvent(event:ScheduledEvent)

        fun showListItemOverflowMenu(event:ScheduledEvent, anchorView:View)
    }

    interface UserInteractionListener {
        fun start()

        fun stop()

        fun onAddPressed()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun onListItemOverflowClicked(event:ScheduledEvent, anchorView: View)

        fun onRemoveEventClicked(event:ScheduledEvent)

        fun onRemoveEventConfirmed(event:ScheduledEvent)

        fun onListItemClicked(event:ScheduledEvent)
    }
}