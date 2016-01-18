package com.n8.intouch.datepicker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
class DatePickerFragment : SwipeableFragment(), Contract.View, DatePickerCard.DateClickedListener {

    var component: DatePickerComponent? = null

    @Inject
    lateinit var contact:Contact

    @Inject
    lateinit var presenter: Contract.UserInteractionListener

    lateinit var datePickerView:DatePickerCard

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("DatePickerComponent must be set")
        }

        component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        datePickerView = inflater?.inflate(R.layout.fragment_date_picker, container, false) as DatePickerCard

        presenter.onContactReceived(contact)

        return datePickerView
    }

    // region Implements Contract.View

    override fun bindEvents(events: List<Event>) {
        datePickerView.dateClickListener = this
        datePickerView.setEvents(contact.events)
    }

    // endregion Implements Contract.View

    // region Implements DatePickerCard.DateClickedListener

    override fun onDateClicked(event: Event) {
        try {
            presenter.onDateSelected(event.date)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    override fun onCustomClicked() {
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
}
