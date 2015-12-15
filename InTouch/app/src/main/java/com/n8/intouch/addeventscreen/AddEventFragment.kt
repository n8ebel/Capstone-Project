package com.n8.intouch.addeventscreen


import android.app.DatePickerDialog
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
import android.support.v7.widget.CardView
import android.support.v7.widget.ThemedSpinnerAdapter
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*

import com.n8.intouch.R
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.di.AddEventComponent
import com.n8.intouch.model.Contact
import com.n8.intouch.model.Event
import com.n8.intouch.setupBackNavigation
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Fragment that allows a user to create a new scheduled event for a contact.
 */
class AddEventFragment : Fragment(), AddEventContract.View, AdapterView.OnItemClickListener {

    val format = SimpleDateFormat("yyyy-MM-dd")

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

    lateinit var contentContainer:ViewGroup

    lateinit var startHeader: StartHeader

    lateinit var datePickerCard: DatePickerCard

    lateinit var datesList: ListView

    lateinit var adapter:ArrayAdapter<Event>

    lateinit var spacer:View

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
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        collapsingToolbar = view.findViewById(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbar.isTitleEnabled = true

        var toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setupBackNavigation { presenter.onNavIconPressed() }

        contactThumbnailImageView = view.findViewById(R.id.contactThumbnail) as ImageView

        contentContainer = view.findViewById(R.id.contentContainer) as ViewGroup
        startHeader = inflater.inflate(R.layout.add_event_header_start, contentContainer, false) as StartHeader

        spacer = inflater.inflate(R.layout.content_spacer, contentContainer, false)

        datePickerCard = inflater.inflate(R.layout.date_picker_card, contentContainer, false) as DatePickerCard
        datesList = datePickerCard.findViewById(R.id.listView) as ListView
        datesList.onItemClickListener = this

        progressBar = ContentLoadingProgressBar(activity)

        presenter.onContactUriReceived(contactUri)

        return view
    }

    override fun onResume() {
        super.onResume()

        contentContainer.addView(startHeader)
        contentContainer.addView(spacer)
        contentContainer.addView(datePickerCard)
    }

    // region Implements OnItemClickListener

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (position == datesList.adapter.count - 1) {
            var cal = Calendar.getInstance()
            DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { view, year, month, day ->
                        var selectedDate = GregorianCalendar(year, month, day)
                        presenter.onDateSelected(selectedDate.timeInMillis)
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE)).
                    show()
        } else {
            try {
                var date = format.parse(adapter.getItem(position).date);
                presenter.onDateSelected(date.time)
            } catch (e:ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // endregion Implements OnItemClickListener

    // region Implements AddEventView

    override fun displayContactInfo(contact: Contact) {
        Toast.makeText(activity, contact.name, Toast.LENGTH_LONG).show();

        collapsingToolbar.title = contact.name

        var roundedThumbnail = RoundedBitmapDrawableFactory.create(activity.resources, contact.thumbnail)
        contactThumbnailImageView.setImageDrawable(roundedThumbnail)

        // Bind the event values
        //
        var spinnerEvents = ArrayList<Event>(contact.events)
        spinnerEvents.add(CustomDateEvent(context.getString(R.string.custom_date)))
        adapter = ArrayAdapter<Event>(activity, android.R.layout.simple_list_item_1, spinnerEvents)
        datesList.adapter = adapter
    }

    override fun displaySelectedDate(timestamp: Long) {
        startHeader.setTitle(format.format(Date(timestamp)))
    }

    override fun updateContinueButton(shown: Boolean) {
        datePickerCard.setContinueButtonEnabled(shown)
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

    private class CustomDateEvent(val msg:String) : Event("Custom", "Custom", "") {

        override fun toString(): String {
            return msg
        }
    }
}
