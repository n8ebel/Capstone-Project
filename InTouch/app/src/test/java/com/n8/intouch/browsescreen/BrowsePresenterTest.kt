package com.n8.intouch.browsescreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.firebase.client.FirebaseError
import com.n8.intouch.BuildConfig
import com.n8.intouch.R
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

    lateinit var mMockActivity:Activity

    lateinit var mCurrentActivityProvider:CurrentActivityProvider

    lateinit var mCurrentUser:User

    lateinit var mViewController:BrowseContract.ViewController

    lateinit var mEventsManager:EventsDataManager

    @Before
    fun setup(){
        mMockActivity = Mockito.mock(Activity::class.java)

        mCurrentActivityProvider = Mockito.mock(CurrentActivityProvider::class.java).apply {
            Mockito.`when`(getCurrentActivity()).thenReturn(mMockActivity)
        }

        mCurrentUser = Mockito.mock(User::class.java)

        mViewController = Mockito.mock(BrowseContract.ViewController::class.java)

        mEventsManager = Mockito.mock(EventsDataManager::class.java)
    }

    @Test
    fun test_Start() {
        val events:List<ScheduledEvent> = listOf()
        val present: BrowsePresenter = createBrowsePresenter()
        var callback = present.getEventsHandler

        Mockito.`when`(mEventsManager.getEvents(callback)).then { callback.invoke(events) }

        present.start()

        verify(mEventsManager, times(1)).getEvents(callback)
        verify(mViewController, times(1)).displayEvents(events)
        verify(mEventsManager, times(1)).addScheduledEventListener(present)
    }

    @Test
    fun test_Stop() {
        val presenter = createBrowsePresenter().apply {
            stop()
        }

        verify(mEventsManager, times(1)).removeScheduledEventListener(presenter)
    }

    @Test
    fun test_onAddPressed() {
        createBrowsePresenter().apply {
            onAddPressed()
        }

        verify(mMockActivity, times(1)).startActivityForResult(BrowsePresenter.PICK_CONTACT_INTENT, BrowsePresenter.REQUEST_PICK_CONTACT)
    }

    @Test
    fun test_onActivityResult_NotResultOk() {
        val notOkayResult = AppCompatActivity.RESULT_OK + 1
        val errorMsg = "invalid"

        Mockito.`when`(mMockActivity.getString(R.string.error_getting_contact_uri)).thenReturn(errorMsg)


        createBrowsePresenter().apply {
            onActivityResult(BrowsePresenter.REQUEST_PICK_CONTACT, notOkayResult, null)
        }

        verifyZeroInteractions(mEventsManager)
        verify(mViewController, times(1)).displayError(errorMsg)
    }

    @Test
    fun test_onActivityResult_ResultOk_NullIntent() {
        val errorMsg = "invalid"

        Mockito.`when`(mMockActivity.getString(R.string.invalid_contact_uri)).thenReturn(errorMsg)


        createBrowsePresenter().apply {
            onActivityResult(BrowsePresenter.REQUEST_PICK_CONTACT, AppCompatActivity.RESULT_OK, null)
        }

        verifyZeroInteractions(mEventsManager)
        verify(mViewController, times(1)).displayError(errorMsg)
    }

    @Test
    fun test_onActivityResult_ResultOk_InvalidUri() {
        val errorMsg = "invalid"
        val mockIntent = Mockito.mock(Intent::class.java)
        Mockito.`when`(mMockActivity.getString(R.string.invalid_contact_uri)).thenReturn(errorMsg)

        createBrowsePresenter().apply {
            onActivityResult(BrowsePresenter.REQUEST_PICK_CONTACT, AppCompatActivity.RESULT_OK, mockIntent)
        }

        verifyZeroInteractions(mEventsManager)
        verify(mViewController, times(1)).displayError(errorMsg)
    }

    @Test
    fun test_onActivityResult_ResultOk_ValidUri() {
        val mockIntent = Mockito.mock(Intent::class.java)
        Mockito.`when`(mockIntent.data).thenReturn(Mockito.mock(Uri::class.java))

        createBrowsePresenter().apply {
            onActivityResult(BrowsePresenter.REQUEST_PICK_CONTACT, AppCompatActivity.RESULT_OK, mockIntent)
        }

        verify(mMockActivity, times(1)).startActivity(any(Intent::class.java))
    }

    @Test
    fun test_onListItemOverflowClicked() {
        val event = ScheduledEvent()
        val mockView = Mockito.mock(View::class.java)

        createBrowsePresenter().apply {
            onListItemOverflowClicked(event, mockView)
        }

        verify(mViewController, times(1)).showListItemOverflowMenu(event, mockView)
    }

    @Test
    fun test_onRemoveEventClicked() {
        val event = ScheduledEvent()

        createBrowsePresenter().apply {
            onRemoveEventClicked(event)
        }

        verify(mViewController, times(1)).promptToRemoveEvent(event)
    }

    @Test
    fun test_onRemoveEventConfirmed_Success() {
        val event = ScheduledEvent()
        val callback = { success:Boolean, error:FirebaseError? -> }

        Mockito.`when`(mEventsManager.removeEvent(event, callback)).then { callback(true, null) }

        createBrowsePresenter().apply {
            onRemoveEventConfirmed(event)
        }

        verifyZeroInteractions(mViewController)
    }

    @Test
    fun test_onRemoveEventConfirmed_Failure() {
        val event = ScheduledEvent()
        val errorMsg = "invalid"
        Mockito.`when`(mMockActivity.getString(R.string.failed_to_remove_event)).thenReturn(errorMsg)

        val presenter = createBrowsePresenter()

        Mockito.`when`(mEventsManager.removeEvent(event, presenter.removeEventhandler)).then {
            presenter.removeEventhandler(false, null)
        }

        presenter.onRemoveEventConfirmed(event)

        verify(mEventsManager, times(1)).removeEvent(event, presenter.removeEventhandler)
        verify(mViewController, times(1)).displayError(errorMsg)
    }

    @Test
    fun test_onListItemClicked() {
        assert(true)
    }

    @Test
    fun test_onScheduledEventAdded() {
        val event = ScheduledEvent()

        createBrowsePresenter().apply {
            onScheduledEventAdded(event, 0)
        }

        verify(mViewController, times(1)).displayAddedEvent(event, 0)
    }

    @Test
    fun test_onScheduledEventRemoved() {
        val event = ScheduledEvent()

        createBrowsePresenter().apply {
            onScheduledEventRemoved(event, 0)
        }

        verify(mViewController, times(1)).hideRemovedEvent(event, 0)
    }

    // region Private Methods

    private fun createBrowsePresenter(): BrowsePresenter {
        return BrowsePresenter(mCurrentActivityProvider, mViewController, mCurrentUser, mEventsManager)
    }

    // endregion Private Methods
}