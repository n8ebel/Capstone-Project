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

class AddEventPresenter(
        val currentActivityProvider: CurrentActivityProvider,
        val viewController: AddEventContract.ViewController,
                        val interactor: ContactLoader,
                        val currentUser: User,
                        val eventManager: EventsDataManager) : AddEventContract.UserInteractionListener {

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
                    datePickerModule(DatePickerModule(contact, fragment, object : DatePickerFragment.Listener{
                        override fun onDateSelected(date: Long) {
                            throw UnsupportedOperationException()
                        }

                    })).
                    build()

            fragment.component = datePickerComponent

            viewController.showDatePicker(fragment)
        })
    }

    override fun onNavIconPressed() {
        viewController.finish()
    }

    override fun scheduleEvent() {
        throw UnsupportedOperationException()
    }
}