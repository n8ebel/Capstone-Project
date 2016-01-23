package com.n8.intouch.addeventscreen


import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.addeventscreen.di.AddEventComponent
import com.n8.intouch.common.BackPressedListener
import com.n8.intouch.datepicker.di.DaggerDatePickerComponent
import com.n8.intouch.datepicker.di.DatePickerModule
import com.n8.intouch.messageentryscreen.di.DaggerMessageEntryComponent
import com.n8.intouch.messageentryscreen.di.MessageEntryModule
import com.n8.intouch.model.Contact
import com.n8.intouch.repeatpicker.MessageEntryFragment
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import com.n8.intouch.repeatpicker.di.DaggerRepeatPickerComponent
import com.n8.intouch.repeatpicker.di.RepeatPickerModule
import com.n8.intouch.setupBackNavigation
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Fragment that allows a user to create a new scheduled event for a contact.
 */
class AddEventFragment : Fragment(), BackPressedListener, AddEventContract.View, DatePickerFragment.Listener, RepeatPickerFragment.Listener {

    // TODO make this locale safe
    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

    var component: AddEventComponent? = null

    @Inject
    lateinit var contactUri:Uri

    @Inject
    lateinit var presenter:AddEventContract.UserInteractionListener

    @Inject
    lateinit var contentResolver:ContentResolver

    lateinit var rootView:ViewGroup

    lateinit var progressBar:ContentLoadingProgressBar

    lateinit var collapsingToolbar:CollapsingToolbarLayout

    lateinit var contactThumbnailPlaceholder:ImageView

    lateinit var contactThumbnailImageView:ImageView

    lateinit var headerTextView:TextView

    //lateinit var datePickerCard: DatePickerCard

    lateinit var cardStack:CardStack

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("AddEventComponent must be set")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        component?.inject(this)

        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_add_for_date, container, false) as ViewGroup

        collapsingToolbar = rootView.findViewById(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbar.isTitleEnabled = true

        var toolbar = rootView.findViewById(R.id.toolbar) as Toolbar
        toolbar.setupBackNavigation { presenter.onNavIconPressed() }

        contactThumbnailPlaceholder = rootView.findViewById(R.id.contactThumbnailPlaceholder) as ImageView
        contactThumbnailImageView = rootView.findViewById(R.id.contactThumbnail) as ImageView

        cardStack = CardStack(childFragmentManager, rootView, R.id.content_container, R.id.fragment_container)

        progressBar = ContentLoadingProgressBar(activity)

        headerTextView = rootView.findViewById(R.id.header_textView) as TextView

        return rootView
    }

    override fun onStart() {
        super.onStart()

        presenter.onContactUriReceived(contactUri)
    }

    // region Implements BackPressedListener

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }

        return false
    }

    // endregion Implements BackPressedListener

    // region Implements AddEventView

    override fun displayContactInfo(contact: Contact) {

        collapsingToolbar.title = contact.name

        if (contact.thumbnail != null) {
            var roundedThumbnail = RoundedBitmapDrawableFactory.create(activity.resources, contact.thumbnail)
            contactThumbnailImageView.setImageDrawable(roundedThumbnail)
            contactThumbnailPlaceholder.visibility = View.GONE
            contactThumbnailImageView.visibility = View.VISIBLE
        }

        showDatePicker(contact)
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
    }

    override fun finish() {
        activity.finish()
    }

    // endregion Implements AddEventView

    // region Implements DatePickerFragment.Listener

    override fun onDateSelected(time: Long) {
        headerTextView.text = DATE_FORMAT.format(Date(time))
        showRepeatPicker()
    }

    // endregion Implements DatePickerFragment.Listener

    // region Implements RepeatPickerFragment.Listener

    override fun onRepeatScheduleSelected(hour: Int, min: Int, interval: Int, duration: Long) {
        showMessageEntry()
    }

    // endregion Implements RepeatPickerFragment.Listener

    // region Private Methods

    private fun showDatePicker(contact: Contact){
        headerTextView.text = getString(R.string.pick_date)

        var fragment = DatePickerFragment()
        var datePickerComponent = DaggerDatePickerComponent.builder().
                applicationComponent(InTouchApplication.graph).
                datePickerModule(DatePickerModule(contact, fragment, this)).
                build()

        fragment.component = datePickerComponent

        cardStack.addView(fragment, "DatePicker", false)
    }

    private fun showRepeatPicker() {
        var fragment = RepeatPickerFragment()

        var component = DaggerRepeatPickerComponent.builder().
                repeatPickerModule(RepeatPickerModule(context, fragment, this)).
                build()
        fragment.component = component

        cardStack.addView(fragment, "RepeatPicker", true)
    }

    private fun showMessageEntry() {
        var fragment = MessageEntryFragment()

        var component = DaggerMessageEntryComponent.builder().
                messageEntryModule(MessageEntryModule()).
                build()
        fragment.component = component

        cardStack.addView(fragment, "MessageEntry", true)
    }

    // endregion Private Methods
}
