package com.n8.intouch.browsescreen

import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.AddEventActivity
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.model.ScheduledEvent
import com.n8.intouch.model.User

class BrowsePresenter(val currentActivityProvider: CurrentActivityProvider,
                      val viewController:BrowseContract.ViewController,
                      val currentUser: User,
                      val eventManager:EventsDataManager) :

        BrowseContract.UserInteractionListener,
        EventsDataManager.Listener {


    val contactsUri = Uri.parse("content://contacts")

    override fun start() {

        eventManager.getEvents { events ->
            viewController.displayEvents(events)
        }

        eventManager.addScheduledEventListener(this)
    }

    override fun stop() {
        eventManager.removeScheduledEventListener(this)
    }

    override fun onAddPressed() {
        var pickContactIntent = Intent(Intent.ACTION_PICK, contactsUri)

        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        currentActivityProvider.getCurrentActivity().startActivityForResult(pickContactIntent, 1);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Make sure the request was successful
        if (resultCode == AppCompatActivity.RESULT_OK) {

            // Get the URI that points to the selected contact
            var contactUri = data?.getData();

            val currentActivity = currentActivityProvider.getCurrentActivity()
            if (contactUri != null) {
                var intent = AddEventActivity.createAddForDateIntent(currentActivity, contactUri)
                currentActivity.startActivity(intent)
            }else{
                viewController.displayError(Throwable(currentActivity.getString(R.string.invalid_contact_uri)))
            }
        }
    }

    override fun onListItemOverflowClicked(event:ScheduledEvent, anchorView: View) {
        viewController.showListItemOverflowMenu(event, anchorView)
    }

    override fun onRemoveEventClicked(event: ScheduledEvent) {
        viewController.promptToRemoveEvent(event)
    }

    override fun onRemoveEventConfirmed(event: ScheduledEvent) {
        eventManager.removeEvent(event, { success, error ->
            if (!success) {
                val currentActivity = currentActivityProvider.getCurrentActivity()
                Toast.makeText(
                        currentActivity,
                        currentActivity.getString(R.string.failed_to_remove_event),
                        Toast.LENGTH_SHORT).
                        show()
            }
        })
    }

    override fun onListItemClicked(event: ScheduledEvent) {
        Toast.makeText(currentActivityProvider.getCurrentActivity(), "Event ${event.id} clicked", Toast.LENGTH_LONG).show()
    }

    // region Implements EventDataManager.Listener

    override fun onScheduledEventAdded(event: ScheduledEvent, index:Int) {
        viewController.displayAddedEvent(event, index)
    }

    override fun onScheduledEventRemoved(event: ScheduledEvent, index:Int) {
        viewController.hideRemovedEvent(event, index)
    }

    // endregion Implements EventDataManager.Listener

}