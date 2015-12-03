package com.n8.intouch.browsescreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.AddEventActivity

/**
 * Created by n8 on 12/2/15.
 */
class TabbedFragmentPresenterImpl(val fragment: Fragment, val view:TabbedFragmentView) : TabbedFragmentPresenter {

    val contactsUri = Uri.parse("content://contacts")

    override fun onAddPressed() {
        var pickContactIntent = Intent(Intent.ACTION_PICK, contactsUri)

        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        fragment.startActivityForResult(pickContactIntent, 1);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Make sure the request was successful
        if (resultCode == AppCompatActivity.RESULT_OK) {

            // Get the URI that points to the selected contact
            var contactUri = data?.getData();

            if (contactUri != null) {
                var intent = AddEventActivity.createAddForDateIntent(fragment.context, contactUri)
                fragment.startActivity(intent)
            }else{
                view.displayError(Throwable(fragment.getString(R.string.invalid_contact_uri)))
            }
        }
    }
}