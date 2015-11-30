package com.n8.intouch.addscreen

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Event
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast

import com.n8.intouch.R

/**
 * Activity to host different fragments that allow the user to create/edit events
 */
class AddActivity : AppCompatActivity() {

    val BACKSTACK_TAG = "add_fragment"

    companion object Factory {
        val ARG_TYPE = "type"

        private val TYPE_ADD_FOR_DATE = "add_for_date"

        private val TYPE_ADD_CUSTOM = "add_custom"

        fun createAddForDateIntent(context:Context): Intent {
            var intent = Intent(context, AddActivity::class.java)
            intent.putExtra(ARG_TYPE, TYPE_ADD_FOR_DATE)
            return intent
        }

        fun createAddForCustomeIntent(context:Context): Intent {
            var intent = Intent(context, AddActivity::class.java)
            intent.putExtra(ARG_TYPE, TYPE_ADD_CUSTOM)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        if (intent?.getStringExtra(ARG_TYPE) == TYPE_ADD_FOR_DATE) {
            showAddForDate(savedInstanceState)
        }else if (intent?.getStringExtra(ARG_TYPE) == TYPE_ADD_CUSTOM) {
            showAddForCustom(savedInstanceState)
        } else {
            throw IllegalStateException("Activity was not launched with valid arguments.  AddActivity.Factory should be used to create intents")
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {

            // Get the URI that points to the selected contact
            var contactUri = data?.getData();

            var projection = arrayOf(ContactsContract.Contacts._ID)

            var cursor = getContentResolver().query(contactUri, projection, null, null, null);
            cursor.moveToFirst();

            // Retrieve the phone number from the NUMBER column
            var idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);

            var id = cursor.getString(idColumn)


            Log.d("TAG", "id: $id")

            var eventsProjection = arrayOf(
                    ContactsContract.Contacts.Entity.RAW_CONTACT_ID,
                    ContactsContract.Contacts.Entity.LOOKUP_KEY,
                    ContactsContract.Contacts.Entity.ACCOUNT_TYPE,
                    ContactsContract.Contacts.Entity.DATA1,
                    ContactsContract.Contacts.Entity.MIMETYPE,
                    ContactsContract.Contacts.Entity.DISPLAY_NAME

            )

            var sortOrder = ContactsContract.Contacts.Entity.RAW_CONTACT_ID + " ASC"

            var rawContactsCursor = contentResolver.query(contactUri, eventsProjection, null, null, sortOrder)

            while(rawContactsCursor.moveToNext()){
                var contact_id = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.RAW_CONTACT_ID));
                var lookupKey = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.LOOKUP_KEY))
                var accountType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.ACCOUNT_TYPE));
                var data1 = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1));
                //final String type = eventsCursor.getString(cursor.getColumnIndex(Event.TYPE));
                var mimeType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.MIMETYPE));
                var displayName = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DISPLAY_NAME));

                Log.d("TAG", "name: $displayName  raw_contact_id: $contact_id  lookupKey: $lookupKey  accountType: $accountType  data1: $data1  mimeType: $mimeType")

                var entityUri = ContentUris.withAppendedId(ContactsContract.RawContactsEntity.CONTENT_URI, contact_id.toLong())

                var emailProjection = arrayOf(
                        ContactsContract.CommonDataKinds.Email._ID,
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.LABEL
                )

                var emailSelection =
                        ContactsContract.Data.LOOKUP_KEY + " = ?" +
                                " AND " +
                                ContactsContract.Data.MIMETYPE + " = " +
                                "'" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'"

                var emailArgs = arrayOf(lookupKey)

                var emailCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, emailProjection, emailSelection, emailArgs, ContactsContract.CommonDataKinds.Email.TYPE + " ASC ")

                while (emailCursor.moveToNext()) {
                    var emailAddress = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))

                    Log.d("TAG", emailAddress)
                }

            }

            // Do something with the phone number...
        }
        return

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showAddForDate(savedInstanceState:Bundle?) {
        if (savedInstanceState != null) {
            return
        }

        supportFragmentManager.beginTransaction().
                add(R.id.fragmentContainer, AddForDateFragment()).
                addToBackStack(BACKSTACK_TAG).
                commit()
    }

    private fun showAddForCustom(savedInstanceState:Bundle?){
        if (savedInstanceState != null) {
            return
        }

        supportFragmentManager.beginTransaction().
                add(R.id.fragmentContainer, AddForCustomFragment()).
                addToBackStack(BACKSTACK_TAG).
                commit()
    }

}
