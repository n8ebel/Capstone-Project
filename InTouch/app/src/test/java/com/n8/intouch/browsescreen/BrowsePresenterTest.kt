package com.n8.intouch.browsescreen

import android.app.Activity
import com.n8.intouch.BuildConfig
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.model.ScheduledEvent
import com.n8.intouch.model.User
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class BrowsePresenterTest {

    val currentActivityProvider = Mockito.mock(CurrentActivityProvider::class.java).apply {
        Mockito.`when`(getCurrentActivity()).thenReturn(Mockito.mock(Activity::class.java))
    }

    val currentUser = Mockito.mock(User::class.java)

    @Before
    fun setup(){

    }

    @Test
    fun testStart() {
        val events:List<ScheduledEvent> = listOf()

        val viewController = Mockito.mock(BrowseContract.ViewController::class.java)
        val eventsmanager = Mockito.mock(EventsDataManager::class.java)

        val present:BrowsePresenter = BrowsePresenter(currentActivityProvider, viewController, currentUser, eventsmanager)
        var callback = present.getEventsHandler

        Mockito.`when`(eventsmanager.getEvents(callback)).then { callback.invoke(events) }

        present.start()

        verify(eventsmanager, times(1)).getEvents(callback)

        verify(viewController, times(1)).displayEvents(events)

        verify(eventsmanager, times(1)).addScheduledEventListener(present)
    }

    @Test
    fun testStop() {
        val viewController = Mockito.mock(BrowseContract.ViewController::class.java)
        val eventsmanager = Mockito.mock(EventsDataManager::class.java)

        val presenter:BrowsePresenter = BrowsePresenter(currentActivityProvider, viewController, currentUser, eventsmanager)

        presenter.stop()

        verify(eventsmanager, times(1)).removeScheduledEventListener(presenter)
    }

    @Test
    fun test_onAddPressed() {
        val activity = Mockito.mock(Activity::class.java)
        val activityProvider = Mockito.mock(CurrentActivityProvider::class.java).apply {
            Mockito.`when`(getCurrentActivity()).thenReturn(activity)
        }

        val viewController = Mockito.mock(BrowseContract.ViewController::class.java)
        val eventsmanager = Mockito.mock(EventsDataManager::class.java)

        val presenter:BrowsePresenter = BrowsePresenter(activityProvider, viewController, currentUser, eventsmanager)

        presenter.onAddPressed()

        verify(activity, times(1)).startActivityForResult(BrowsePresenter.PICK_CONTACT_INTENT, BrowsePresenter.REQUEST_PICK_CONTACT)
    }
}