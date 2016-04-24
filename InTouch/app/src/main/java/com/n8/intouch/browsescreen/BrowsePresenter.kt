package com.n8.intouch.browsescreen

import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.client.FirebaseError
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.AddEventActivity
import com.n8.intouch.alarm.EventScheduler
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.model.ScheduledEvent
import com.n8.intouch.model.User

open class BrowsePresenter(val currentActivityProvider: CurrentActivityProvider,
                      val viewController:BrowseContract.ViewController,
                      val currentUser: User,
                      val eventManager:EventsDataManager,
                           val mEventScheduler:EventScheduler) :

        BrowseContract.UserInteractionListener,
        EventsDataManager.Listener {


    companion object {
        val REQUEST_PICK_CONTACT = 1

        val contactsUri = Uri.parse("content://contacts")

        val PICK_CONTACT_INTENT = Intent(Intent.ACTION_PICK, contactsUri).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
    }



    val getEventsHandler = { events:List<ScheduledEvent> ->
        viewController.hideProgress()
        viewController.displayEvents(events)

        if(events.size > 0) {
            viewController.hideNoContentView()
        }else{
            viewController.showNoContentView()
        }

        events.forEach { mEventScheduler.scheduleEvent(it) }
    }

    val removeEventhandler = { success:Boolean, error:FirebaseError? ->
        if (!success) {
            val currentActivity = currentActivityProvider.getCurrentActivity()
            viewController.displayError(currentActivity.getString(R.string.failed_to_remove_event))
        }
    }

    override fun start() {
        viewController.showProgress()
        eventManager.refreshEvents(getEventsHandler)
        eventManager.addScheduledEventListener(this)
    }

    override fun stop() {
        eventManager.removeScheduledEventListener(this)
    }

    override fun onAddPressed() {
        currentActivityProvider.getCurrentActivity().startActivityForResult(PICK_CONTACT_INTENT, REQUEST_PICK_CONTACT);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val currentActivity = currentActivityProvider.getCurrentActivity()

        // Make sure the request was successful
        if (resultCode == AppCompatActivity.RESULT_OK) {

            // Get the URI that points to the selected contact
            var contactUri = data?.getData();

            if (contactUri != null) {
                var intent = AddEventActivity.createAddForDateIntent(currentActivity, contactUri)
                currentActivity.startActivity(intent)
            } else {
                viewController.displayError(currentActivity.getString(R.string.invalid_contact_uri))
            }
        } else {
            viewController.displayError(currentActivity.getString(R.string.error_getting_contact_uri))
        }
    }

    override fun onListItemOverflowClicked(event:ScheduledEvent, anchorView: View) {
        viewController.showListItemOverflowMenu(event, anchorView)
    }

    override fun onRemoveEventClicked(event: ScheduledEvent) {
        viewController.promptToRemoveEvent(event)
    }

    override fun onRemoveEventConfirmed(event: ScheduledEvent) {
        eventManager.removeEvent(event, removeEventhandler)
        mEventScheduler.cancelScheduledEvent(event)
    }

    override fun onListItemClicked(event: ScheduledEvent) {
        Toast.makeText(currentActivityProvider.getCurrentActivity(),
                "Event ${event.id} clicked.  Need to finish implementing this add update tests",
                Toast.LENGTH_LONG).show()
    }

    // region Implements EventDataManager.Listener

    override fun onScheduledEventAdded(event: ScheduledEvent, index:Int) {
        viewController.hideNoContentView()
        viewController.displayAddedEvent(event, index)
        mEventScheduler.scheduleEvent(event)
    }

    override fun onScheduledEventRemoved(event: ScheduledEvent, index:Int) {
        if (eventManager.getNumberOfEvents() == 0) {
            viewController.showNoContentView()
        }
        viewController.hideRemovedEvent(event, index)
        mEventScheduler.cancelScheduledEvent(event)
    }

    // endregion Implements EventDataManager.Listener

}