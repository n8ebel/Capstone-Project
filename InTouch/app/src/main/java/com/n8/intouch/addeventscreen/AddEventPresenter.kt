package com.n8.intouch.addeventscreen

import android.net.Uri
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.datepicker.di.DaggerDatePickerComponent
import com.n8.intouch.datepicker.di.DatePickerModule
import com.n8.intouch.getComponent
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
                        val eventManager: EventsDataManager) :
        AddEventContract.UserInteractionListener ,
        DatePickerFragment.Listener,
        RepeatPickerFragment.Listener
{

    companion object {
        // TODO make this locale safe
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

        val TAG_DATE_PICKER = "tag_date_picker"
        val TAG_REPEAT_PICKER = "tag_repeat_picker"
    }

    var startDateTimestamp = -1L

    override fun start() {
        eventManager.getEvents {

        }
    }

    override fun stop() {

    }

    override fun onContactUriReceived(contactUri: Uri) {
        viewController.showProgress()

        interactor.loadContact(contactUri, { contact ->

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
        throw UnsupportedOperationException()
    }

    // region Implements DatePickerFragment.Listener

    override fun onDateSelected(time: Long) {
        startDateTimestamp = time
        viewController.setHeaderText(DATE_FORMAT.format(Date(time)))

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

    override fun onRepeatScheduleSelected(startHour: Int, startMin: Int, interval: Int, duration: Long) {
        throw UnsupportedOperationException()
    }

    // endregion Implements RepeatPickerFragment.Listener
}