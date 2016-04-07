package com.n8.intouch.data

import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.n8.intouch.model.Event
import java.util.*

class FirebaseEventsDataManager(private val firebase: Firebase) : EventsDataManager {

    // region Implements EventsDataManager

    override fun getEvents(function:(List<Event>) -> Unit) {

        firebase.child(firebase.auth.uid).child("events").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapShot: DataSnapshot?) {
                var eventsList:MutableList<Event> = ArrayList()
                dataSnapShot?.children?.forEach {
                    var eventMap = it.value as HashMap<String, Any>

                    eventsList.add(Event("","","", eventMap.get("message") as String))
                }
                function(eventsList)
            }

            override fun onCancelled(firebaseError: FirebaseError?) {
                function(emptyList())
            }

        });

        function(emptyList())
    }

    // endregion Implements EventsDataManager

}