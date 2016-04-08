package com.n8.intouch.data

import android.util.Log
import com.firebase.client.*
import com.n8.intouch.model.ScheduledEvent
import java.util.*

class FirebaseEventsDataManager(private val firebase: Firebase) : EventsDataManager {

    companion object {
        val TAG = "FirebaseEventsDataManager"
        val EVENTS_PATH = "events"
    }

    // region Implements EventsDataManager

    override fun getEvents(function:(List<ScheduledEvent>) -> Unit) {

        getEventsRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapShot: DataSnapshot?) {
                var eventsList:MutableList<ScheduledEvent> = ArrayList()
                dataSnapShot?.children?.forEach {
                    try {
                        val event = it.getValue(ScheduledEvent::class.java)
                        eventsList.add(event)
                    } catch(e: FirebaseException){
                        Log.d(TAG, "Failed to parse dataSnapshot: " + e.message)
                    }

                }
                function(eventsList)
            }

            override fun onCancelled(firebaseError: FirebaseError?) {
                function(emptyList())
            }

        });

        function(emptyList())
    }

    override fun addEvent(event: ScheduledEvent, function: (Boolean, FirebaseError?) -> Unit) {
        getEventsRef().push().setValue(event, Firebase.CompletionListener { error, firebase ->
                    function(error == null, error)
                })
    }

    // endregion Implements EventsDataManager

    // region Private Methods

    fun getEventsRef(): Firebase {
        return firebase.child(firebase.auth.uid).child(EVENTS_PATH)
    }

    // endregion Private Methods

}