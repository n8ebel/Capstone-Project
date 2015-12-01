package com.n8.intouch.addeventscreen


import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.n8.intouch.InTouchApplication

import com.n8.intouch.R
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddEventFragment : Fragment(), AddEventView {

    val component = createComponent()

    @Inject
    lateinit var presenter:AddEventPresenter

    @Inject
    lateinit var contentResolver:ContentResolver

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        var pickContactFab = view.findViewById(R.id.fab) as FloatingActionButton
        pickContactFab.setOnClickListener {
            pickContact()
        }

        component.inject(this)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Make sure the request was successful
        if (resultCode == AppCompatActivity.RESULT_OK) {

            // Get the URI that points to the selected contact
            var contactUri = data?.getData();

            var projection = arrayOf(ContactsContract.Contacts._ID)

            var cursor = contentResolver.query(contactUri, projection, null, null, null);
            cursor.moveToFirst();

            // Retrieve the phone number from the NUMBER column
            var idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);

            var id = cursor.getString(idColumn)


            Log.d("TAG", "id: $id")

            var foo = arrayOf(
                    ContactsContract.Contacts.Entity.RAW_CONTACT_ID,
                    ContactsContract.Contacts.Entity.LOOKUP_KEY,
                    ContactsContract.Contacts.Entity.ACCOUNT_TYPE,
                    ContactsContract.Contacts.Entity.DATA1,
                    ContactsContract.Contacts.Entity.MIMETYPE,
                    ContactsContract.Contacts.Entity.DISPLAY_NAME

            )

            var sortOrder = ContactsContract.Contacts.Entity.RAW_CONTACT_ID + " ASC"

            var rawContactsCursor = contentResolver.query(contactUri, foo, null, null, sortOrder)

            while(rawContactsCursor.moveToNext()){
                var contact_id = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.RAW_CONTACT_ID));
                var lookupKey = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.LOOKUP_KEY))
                var accountType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.ACCOUNT_TYPE));
                var data1 = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1));
                //final String type = eventsCursor.getString(cursor.getColumnIndex(Event.TYPE));
                var mimeType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.MIMETYPE));
                var displayName = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DISPLAY_NAME));

                var contactInfo = "name: $displayName  raw_contact_id: $contact_id  lookupKey: $lookupKey  accountType: $accountType  data1: $data1  mimeType: $mimeType"

                presenter.onContactDataLoaded(contactInfo)

                var eventProjectiong = arrayOf(
                        ContactsContract.CommonDataKinds.Event._ID,
                        ContactsContract.CommonDataKinds.Event.START_DATE,
                        ContactsContract.CommonDataKinds.Event.TYPE,
                        ContactsContract.CommonDataKinds.Event.LABEL
                )

                var eventSelection =
                        ContactsContract.Data.LOOKUP_KEY + " = ?" +
                                " AND " +
                                ContactsContract.Data.MIMETYPE + " = " +
                                "'" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'"

                var eventArgs = arrayOf(lookupKey)

                var eventCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, eventProjectiong, eventSelection, eventArgs, ContactsContract.CommonDataKinds.Event.TYPE + " ASC ")

                while (eventCursor.moveToNext()) {
                    var eventDate = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                    var eventType = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))

                    when (eventType.toInt()) {
                        ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> eventType = "Anniversary"
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> eventType = "Birthday"
                        ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> eventType = "Custom"
                        ContactsContract.CommonDataKinds.Event.TYPE_OTHER -> eventType = "Other"
                    }

                    var eventLabel = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL))

                    Log.d("TAG", "label: $eventLabel  type: $eventType  date: $eventDate")
                }

            }

            // Do something with the phone number...
        }
        return

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun displayContactInfo(contactInfo: String) {
        Toast.makeText(activity, contactInfo, Toast.LENGTH_LONG).show();
    }

    private fun pickContact() {
        var pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, 1);
    }

    private fun createComponent() : AddEventComponent {
        return DaggerAddEventComponent.builder().addEventModule(AddEventModule(this)).applicationComponent(InTouchApplication.graph).build()
    }
}
