package com.n8.intouch.browsescreen

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
                      val eventManager:EventsDataManager) : BrowseContract.UserInteractionListener {

    val contactsUri = Uri.parse("content://contacts")

    override fun start() {

        eventManager.getEvents { events ->
            viewController.displayEvents(events)
        }
    }

    override fun stop() {

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

    override fun onListItemOverflowClicked(view: View) {
        Toast.makeText(currentActivityProvider.getCurrentActivity(), "Overflow clicked", Toast.LENGTH_LONG).show()
    }

    override fun onRemoveEventClicked(event: ScheduledEvent) {
        Toast.makeText(currentActivityProvider.getCurrentActivity(), "Remove clicked", Toast.LENGTH_LONG).show()
    }

    override fun onListItemClicked(event: ScheduledEvent) {
        Toast.makeText(currentActivityProvider.getCurrentActivity(), "Event ${event.scheduledMessage} clicked", Toast.LENGTH_LONG).show()
    }

}