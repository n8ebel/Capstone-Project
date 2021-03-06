package com.n8.intouch.addeventscreen

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.PersistableBundle
import android.text.format.DateUtils
import com.firebase.client.FirebaseError
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.alarm.EventScheduler
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.common.SchedulingUtils
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.datepicker.di.DaggerDatePickerComponent
import com.n8.intouch.datepicker.di.DatePickerModule
import com.n8.intouch.getComponent
import com.n8.intouch.messageentryscreen.MessageEntryFragment
import com.n8.intouch.messageentryscreen.di.DaggerMessageEntryComponent
import com.n8.intouch.messageentryscreen.di.MessageEntryModule
import com.n8.intouch.model.Contact
import com.n8.intouch.model.ScheduledEvent
import com.n8.intouch.model.User
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import com.n8.intouch.repeatpicker.di.DaggerRepeatPickerComponent
import com.n8.intouch.repeatpicker.di.RepeatPickerModule
import java.text.SimpleDateFormat
import java.util.*

class AddEventPresenter(
        val currentActivityProvider: CurrentActivityProvider,
        val viewController: AddEventContract.ViewController,
                        val interactor: ContactLoader,
                        val currentUser: User,
                        val eventManager: EventsDataManager,
                        val mEventScheduler: EventScheduler) :
        AddEventContract.UserInteractionListener ,
        DatePickerFragment.Listener,
        RepeatPickerFragment.Listener,
        MessageEntryFragment.Listener
{

    companion object {

        val TAG_DATE_PICKER = "tag_date_picker"
        val TAG_REPEAT_PICKER = "tag_repeat_picker"
        val TAG_MESSAGE_ENTRY = "tag_message_entry"
    }

    lateinit var mContact: Contact

    var startDateTimestamp = -1L

    var startDateHour = -1

    var startDateMin = -1

    var repeatInterval = -1  // value such as '1' or '3'

    var repeatDuration = -1L  // value such as week.inMillis()

    var scheduledMessage = ""

    var mPhoneNumber = ""

    override fun start() {

    }

    override fun stop() {

    }

    override fun onContactUriReceived(contactUri: Uri) {
        viewController.showProgress()

        interactor.loadContact(contactUri, { contact ->
            mContact = contact

            val currentActivity = currentActivityProvider.getCurrentActivity()

            viewController.hideProgress()

            viewController.setHeaderText(currentActivity.getString(R.string.pick_date))

            viewController.displayContactInfo(contact)

            var fragment = DatePickerFragment()
            var datePickerComponent = DaggerDatePickerComponent.builder().
                    applicationComponent(currentActivity.application.getComponent()).
                    datePickerModule(DatePickerModule(contact, fragment, this)).
                    build()

            fragment.component = datePickerComponent

            viewController.showSwipeableFragment(fragment, TAG_DATE_PICKER, false)
        })
    }

    override fun onNavIconPressed() {
        viewController.finish()
    }

    override fun scheduleEvent() {
        eventManager.addEvent(
                startDateTimestamp, startDateHour, startDateMin, repeatInterval, repeatDuration, scheduledMessage, mPhoneNumber,
                { event, error ->
                    if (event != null) {
                        viewController.finish()
                    } else {
                        viewController.displayError(Throwable(error?.message))
                    }
                }
        )
    }

    // region Implements DatePickerFragment.Listener

    override fun onDateSelected(time: Long) {
        startDateTimestamp = time
        viewController.setHeaderText(SchedulingUtils.getDateTimeDisplayString(time))

        val currentActivity = currentActivityProvider.getCurrentActivity()
        var fragment = RepeatPickerFragment().apply {
            component = DaggerRepeatPickerComponent.builder().
                    repeatPickerModule(RepeatPickerModule(currentActivity, this, this@AddEventPresenter)).
                    build()
        }

        viewController.showSwipeableFragment(fragment , TAG_REPEAT_PICKER, true)

    }

    // endregion Implements DatePickerFragment.Listener

    // region Implements RepeatPickerFragment.Listener

    override fun onRepeatScheduleSelected(hour: Int, min: Int, interval: Int, duration: Long) {
        startDateHour = hour
        startDateMin = min
        repeatInterval = interval
        repeatDuration = duration

        showMessageEntry()
    }

    // endregion Implements RepeatPickerFragment.Listener

    // region Implements MessageEntryFragment.Listener

    override fun onMessageEntered(phoneNumber:String, message: String) {
        mPhoneNumber = phoneNumber
        scheduledMessage = message

        val title = getCurrentActivity().getString(R.string.schedule_event_dialog_title)
        val msg = getCurrentActivity().
                        getString(R.string.schedule_event_dialog_message_format,
                        mPhoneNumber,
                        SchedulingUtils.getDateTimeDisplayString(startDateTimestamp),
                        "$repeatInterval ${displayUnitsForRepeatDuration(repeatDuration)}",
                        scheduledMessage)

       viewController.promptToConfirmScheduledEvent(title, msg)
    }

    // endregion Implements MessageEntryFragment.Listener

    // region Private Methods

    private fun showMessageEntry() {
        var fragment = MessageEntryFragment().apply {
            component = DaggerMessageEntryComponent.builder().
                    messageEntryModule(MessageEntryModule(mContact.phoneNumber, this, this@AddEventPresenter)).
                    build()
        }

        viewController.showSwipeableFragment(fragment, TAG_MESSAGE_ENTRY, true)
    }

    private fun displayUnitsForRepeatDuration(duration:Long) : String {
        val currentActivity = currentActivityProvider.getCurrentActivity()

        return SchedulingUtils.getDisplayStringForDuration(getCurrentActivity(), duration)
    }

    private fun getCurrentActivity() : Activity {
        return currentActivityProvider.getCurrentActivity()
    }

    // endregion Private Methods
}