package com.n8.intouch.datepicker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.datepicker.di.DatePickerComponent
import com.n8.intouch.model.Contact
import com.n8.intouch.model.Event
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Displays a DatePickerView and allows the user to choose either a
 * saved date for a contact or a custom date from a picker.
 */
class DatePickerFragment : SwipeableFragment(), Contract.View {

    interface Listener {
        fun onDateSelected(date:Long)

        fun onContinueClicked()
    }

    var component: DatePickerComponent? = null

    @Inject
    lateinit var contact:Contact

    @Inject
    lateinit var presenter: Contract.UserInteractionListener

    lateinit var continueButton: FloatingActionButton

    lateinit var datesList: ListView

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("DatePickerComponent must be set")
        }

        component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView = inflater!!.inflate(R.layout.fragment_date_picker, container, false)

        datesList = rootView!!.findViewById(R.id.listView) as ListView
        datesList.setOnItemClickListener { adapterView, view, position, l ->
            if (position == datesList.adapter.count - 1) {
                onCustomClicked()
            } else {
                onDateClicked(datesList.adapter.getItem(position) as Event)
            }
        }

        continueButton = rootView.findViewById(R.id.continue_button) as FloatingActionButton
        continueButton.setOnClickListener(View.OnClickListener { view ->
            presenter.onContinueClicked()
        })

        presenter.onContactReceived(contact)

        return rootView
    }

    // region Implements Contract.View

    override fun bindEvents(events: List<Event>) {
        var spinnerEvents = ArrayList<Event>(events)
        spinnerEvents.add(CustomDateEvent(context.getString(R.string.custom_date)))
        datesList.adapter = ArrayAdapter<Event>(context, android.R.layout.simple_list_item_1, spinnerEvents)
    }

    override fun setContinueButtonVisible(visible: Boolean) {
        if (visible) continueButton.show() else continueButton.hide()
    }

    // endregion Implements Contract.View

    // region Implements DatePickerCard.DateClickedListener

    fun onDateClicked(event: Event) {
        try {
            presenter.onDateSelected(event.date)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    fun onCustomClicked() {
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
    }

    // endregion Implements DatePickerCard.DateClickedListener

    private class CustomDateEvent(val msg:String) : Event("Custom", "Custom", "1900/01/01") {

        override fun toString(): String {
            return msg
        }
    }
}
