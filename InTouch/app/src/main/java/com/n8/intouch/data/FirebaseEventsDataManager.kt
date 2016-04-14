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
                        Log.e(TAG, "Failed to parse dataSnapshot: " + e.message)
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

    override fun addEvent(startDateTimestamp: Long, startDateHour: Int, startDateMin: Int, repeatInterval: Int, repeatDuration: Long, scheduledMessage: String, function: (Boolean, FirebaseError?) -> Unit) {
        getEventsRef().push().apply {
            val newEvent = ScheduledEvent(key, startDateTimestamp, startDateHour, startDateMin, repeatInterval, repeatDuration, scheduledMessage)
            setValue(newEvent, Firebase.CompletionListener { error, firebase ->
                function(error == null, error)
            })
        }
    }

    override fun removeEvent(event: ScheduledEvent, function: (Boolean, FirebaseError?) -> Unit) {
        getEventsRef().child(event.id).removeValue { error, firebase ->
            function(error == null, error)
        }
    }

    // endregion Implements EventsDataManager

    // region Private Methods

    fun getEventsRef(): Firebase {
        return firebase.child(firebase.auth.uid).child(EVENTS_PATH)
    }

    // endregion Private Methods

}