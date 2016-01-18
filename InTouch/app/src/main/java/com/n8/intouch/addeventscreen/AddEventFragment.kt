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
import android.os.Handler
import android.provider.ContactsContract
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.ThemedSpinnerAdapter
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import com.n8.intouch.datepicker.DatePickerFragment
import com.n8.intouch.addeventscreen.di.AddEventComponent
import com.n8.intouch.datepicker.di.DaggerDatePickerComponent
import com.n8.intouch.datepicker.di.DatePickerModule
import com.n8.intouch.model.Contact
import com.n8.intouch.setupBackNavigation
import javax.inject.Inject

/**
 * Fragment that allows a user to create a new scheduled event for a contact.
 */
class AddEventFragment : Fragment(), AddEventContract.View {

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

    override fun displaySelectedDate(timestamp: Long) {
        //startHeader.setTitle(format.format(Date(timestamp)))
    }

    override fun updateContinueButton(shown: Boolean) {
        //datePickerCard.setContinueButtonEnabled(shown)
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

    override fun displayRepeatPicker() {
        throw NotImplementedError()
//        var repeatPicker = activity.layoutInflater.inflate(R.layout.repeat_picker_card, rootView, false) as RepeatPickerCard
//        var repeatHeader = activity.layoutInflater.inflate(R.layout.add_event_header_repeat, rootView, false) as RepeatHeader
//
//        cardStack.addView(repeatHeader, repeatPicker)
    }

    // endregion Implements AddEventView

    // region Private Methods

    private fun showDatePicker(contact: Contact){
        headerTextView.text = getString(R.string.pick_date)

        var fragment = DatePickerFragment()
        var datePickerComponent = DaggerDatePickerComponent.builder().
                applicationComponent(InTouchApplication.graph).
                datePickerModule(DatePickerModule(contact, fragment)).
                build()

        fragment.component = datePickerComponent

        cardStack.addView(fragment, "DatePicker", false)
    }

    // endregion Private Methods
}
