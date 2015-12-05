package com.n8.intouch.addeventscreen.data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

/**
 * Created by n8 on 11/30/15.
 */
class ContentProviderContactLoader(val context: Context, val contactUri: Uri) : ContactLoader {

    // TODO This should be done on background thread
    override fun loadContact(contactUri: Uri, listener: (ContactLoader.Contact) -> Unit) {
        var projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        var cursor = context.contentResolver.query(contactUri, projection, null, null, null);

        if (!cursor.moveToFirst()) {
            listener.invoke(ContactLoader.Contact("Error"))
            return
        }

        var displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

        listener.invoke(ContactLoader.Contact(displayName))
    }
    //    var projection = arrayOf(ContactsContract.Contacts._ID)
    //
    //    var cursor = fragment.context.contentResolver.query(contactUri, projection, null, null, null);
    //    cursor.moveToFirst();
    //
    //    // Retrieve the phone number from the NUMBER column
    //    var idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
    //
    //    var id = cursor.getString(idColumn)
    //
    //
    //    Log.d("TAG", "id: $id")
    //
    //    var foo = arrayOf(
    //            ContactsContract.Contacts.Entity.RAW_CONTACT_ID,
    //            ContactsContract.Contacts.Entity.LOOKUP_KEY,
    //            ContactsContract.Contacts.Entity.ACCOUNT_TYPE,
    //            ContactsContract.Contacts.Entity.DATA1,
    //            ContactsContract.Contacts.Entity.MIMETYPE,
    //            ContactsContract.Contacts.Entity.DISPLAY_NAME
    //
    //    )
    //
    //    var sortOrder = ContactsContract.Contacts.Entity.RAW_CONTACT_ID + " ASC"
    //
    //    var rawContactsCursor = fragment.context.contentResolver.query(contactUri, foo, null, null, sortOrder)
    //
    //    while(rawContactsCursor.moveToNext()){
    //        var contact_id = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.RAW_CONTACT_ID));
    //        var lookupKey = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.LOOKUP_KEY))
    //        var accountType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.ACCOUNT_TYPE));
    //        var data1 = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1));
    //        //final String type = eventsCursor.getString(cursor.getColumnIndex(Event.TYPE));
    //        var mimeType = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.MIMETYPE));
    //        var displayName = rawContactsCursor.getString(rawContactsCursor.getColumnIndex(ContactsContract.Contacts.Entity.DISPLAY_NAME));
    //
    //        var contactInfo = "name: $displayName  raw_contact_id: $contact_id  lookupKey: $lookupKey  accountType: $accountType  data1: $data1  mimeType: $mimeType"
    //
    //        presenter.onContactDataLoaded(contactInfo)
    //
    //        var eventProjectiong = arrayOf(
    //                ContactsContract.CommonDataKinds.Event._ID,
    //                ContactsContract.CommonDataKinds.Event.START_DATE,
    //                ContactsContract.CommonDataKinds.Event.TYPE,
    //                ContactsContract.CommonDataKinds.Event.LABEL
    //        )
    //
    //        var eventSelection =
    //                ContactsContract.Data.LOOKUP_KEY + " = ?" +
    //                        " AND " +
    //                        ContactsContract.Data.MIMETYPE + " = " +
    //                        "'" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'"
    //
    //        var eventArgs = arrayOf(lookupKey)
    //
    //        var eventCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, eventProjectiong, eventSelection, eventArgs, ContactsContract.CommonDataKinds.Event.TYPE + " ASC ")
    //
    //        while (eventCursor.moveToNext()) {
    //            var eventDate = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
    //            var eventType = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))
    //
    //            when (eventType.toInt()) {
    //                ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> eventType = "Anniversary"
    //                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> eventType = "Birthday"
    //                ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> eventType = "Custom"
    //                ContactsContract.CommonDataKinds.Event.TYPE_OTHER -> eventType = "Other"
    //            }
    //
    //            var eventLabel = eventCursor.getString(eventCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL))
    //
    //            Log.d("TAG", "label: $eventLabel  type: $eventType  date: $eventDate")
    //        }
    //
    //    }
    //
    //    // Do something with the phone number...
}