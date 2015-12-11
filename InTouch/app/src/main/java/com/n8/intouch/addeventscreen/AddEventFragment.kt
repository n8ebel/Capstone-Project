package com.n8.intouch.addeventscreen


import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ThemedSpinnerAdapter
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.n8.intouch.R
import com.n8.intouch.addeventscreen.cards.DatePickerCard
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.di.AddEventComponent
import com.n8.intouch.model.Contact
import com.n8.intouch.model.Event
import com.n8.intouch.setupBackNavigation
import java.util.*
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

    lateinit var progressBar:ContentLoadingProgressBar

    lateinit var collapsingToolbar:CollapsingToolbarLayout

    lateinit var contactThumbnailImageView:ImageView

    lateinit var spinner:Spinner

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("AddEventComponent must be set")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        collapsingToolbar = view.findViewById(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbar.isTitleEnabled = true

        var toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setupBackNavigation { presenter.onNavIconPressed() }

        contactThumbnailImageView = view.findViewById(R.id.contactThumbnail) as ImageView

        spinner = view.findViewById(R.id.spinner) as Spinner

        var contentContainer = view.findViewById(R.id.contentContainer) as ViewGroup
        var startHeader = inflater.inflate(R.layout.add_event_header_start, contentContainer, false)
        var spacer = inflater.inflate(R.layout.content_spacer, contentContainer, false)
        var datePickerCard = DatePickerCard(context)
        datePickerCard.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200)

        contentContainer.addView(startHeader, 0)
        //contentContainer.addView(spacer)
        contentContainer.addView(datePickerCard)

        component?.inject(this)

        progressBar = ContentLoadingProgressBar(activity)

        presenter.onContactUriReceived(contactUri)

        return view
    }

    // region Implements AddEventView

    override fun displayContactInfo(contact: Contact) {
        Toast.makeText(activity, contact.name, Toast.LENGTH_LONG).show();

        collapsingToolbar.title = contact.name

        var roundedThumbnail = RoundedBitmapDrawableFactory.create(activity.resources, contact.thumbnail)
        contactThumbnailImageView.setImageDrawable(roundedThumbnail)

        // Bind the event values
        //
        var spinnerDisplayValues = ArrayList<String>()
        var events = contact.events
        for (event in events) {
            spinnerDisplayValues.add(event.date)
        }

        var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinnerDisplayValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
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
}
