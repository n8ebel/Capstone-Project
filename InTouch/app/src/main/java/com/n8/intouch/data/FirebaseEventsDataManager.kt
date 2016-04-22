package com.n8.intouch.data

import android.util.Log
import com.firebase.client.*
import com.n8.intouch.model.ScheduledEvent
import java.util.concurrent.CopyOnWriteArraySet

class FirebaseEventsDataManager(private val firebase: Firebase) : EventsDataManager, ChildEventListener {

    companion object {
        val TAG = "FirebaseEventsDataManager"
        val EVENTS_PATH = "events"
    }

    private val mScheduledEventsMap: MutableMap<String, ScheduledEvent> = hashMapOf()
    private val mScheduledEventsList: MutableList<ScheduledEvent> = mutableListOf()

    private val mScheduledEventListeners = CopyOnWriteArraySet<EventsDataManager.Listener>()

    init {
        getEventsRef().addChildEventListener(this)
    }

    override fun addScheduledEventListener(listener: EventsDataManager.Listener) {
        mScheduledEventListeners.add(listener)
    }

    override fun removeScheduledEventListener(listener: EventsDataManager.Listener) {
        mScheduledEventListeners.remove(listener)
    }

    // region Implements EventsDataManager

    override fun getNumberOfEvents(): Int {
        return mScheduledEventsList.size
    }

    override fun getEvents(function:(List<ScheduledEvent>) -> Unit) {
        val events:List<ScheduledEvent> = mScheduledEventsMap.flatMap { entry -> listOf(entry.value)  }
        function(events)
    }

    override fun addEvent(startDateTimestamp: Long, startDateHour: Int, startDateMin: Int, repeatInterval: Int, repeatDuration: Long, scheduledMessage: String, phoneNumber:String, function: (event:ScheduledEvent?, FirebaseError?) -> Unit) {
        getEventsRef().push().apply {
            val newEvent = ScheduledEvent(key, startDateTimestamp, startDateHour, startDateMin, repeatInterval, repeatDuration, scheduledMessage, phoneNumber)
            setValue(newEvent, Firebase.CompletionListener { error, firebase ->
                if(error == null) function(newEvent, error) else function(null, error)
            })
        }
    }

    override fun removeEvent(event: ScheduledEvent, function: (Boolean, FirebaseError?) -> Unit) {
        getEventsRef().child(event.id).removeValue { error, firebase ->
            function(error == null, error)
        }
    }

    override fun getEvent(id: String, function: (ScheduledEvent?, FirebaseError?) -> Unit) {
        getEventsRef().child(id).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val event = createEventFromSnapshot(dataSnapshot)
                function(event, null)
            }

            override fun onCancelled(error: FirebaseError?) {
                function(null, error)
            }

        })
    }

    // endregion Implements EventsDataManager

    // region Implements ChildEventListener

    override fun onChildRemoved(snapshot: DataSnapshot?) {
        val event = createEventFromSnapshot(snapshot)
        if (event != null) {
            removeScheduledEvent(event)
        }
    }

    override fun onCancelled(p0: FirebaseError?) {
        throw UnsupportedOperationException()
    }

    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
        throw UnsupportedOperationException()
    }

    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

    }

    override fun onChildAdded(snapshot: DataSnapshot?, previousChildKey: String?) {
        val event = createEventFromSnapshot(snapshot)
        if (event != null) {
            addScheduledEvent(event)
        }
    }


    // endregion Implements ChildEventListener

    // region Private Methods

    private fun createEventFromSnapshot(snapshot: DataSnapshot?) : ScheduledEvent? {
        try {
            return snapshot?.getValue(ScheduledEvent::class.java)
        } catch(e: FirebaseException) {
            Log.e(TAG, "Failed to parse dataSnapshot: " + e.message)
            return null
        }
    }

    fun getEventsRef(): Firebase {
        return firebase.child(firebase.auth.uid).child(EVENTS_PATH)
    }

    private fun addScheduledEvent(event: ScheduledEvent) {
        synchronized(mScheduledEventsMap){
            mScheduledEventsMap.put(event.id, event)
            mScheduledEventsList.add(event)
        }

        mScheduledEventListeners.forEach { it.onScheduledEventAdded(event, mScheduledEventsList.indexOf(event)) }
    }

    private fun removeScheduledEvent(event: ScheduledEvent) {
        var index = mScheduledEventsList.indexOf(event)

        synchronized(mScheduledEventsMap){
            mScheduledEventsMap.remove(event.id)
            mScheduledEventsList.remove(event)
        }

        if(index != -1){
            mScheduledEventListeners.forEach { it.onScheduledEventRemoved(event, index) }
        }
    }

    // endregion Private Methods

}