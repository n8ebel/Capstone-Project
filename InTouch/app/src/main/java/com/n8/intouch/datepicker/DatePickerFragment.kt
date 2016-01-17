package com.n8.intouch.datepicker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.n8.intouch.R
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
class DatePickerFragment : Fragment(), Contract.View, AdapterView.OnItemClickListener {

    val format = SimpleDateFormat("yyyy-MM-dd")

    var component: DatePickerComponent? = null

   // @Inject
    lateinit var contact:Contact

    //@Inject
    lateinit var presenter: Contract.UserInteractionListener

    lateinit var datesList: ListView

    lateinit var adapter: ArrayAdapter<Event>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("DatePickerComponent must be set")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)

        component?.inject(this)

        // Bind the event values
        //
        var spinnerEvents = ArrayList<Event>(contact.events)
        spinnerEvents.add(CustomDateEvent(context.getString(R.string.custom_date)))
        adapter = ArrayAdapter<Event>(activity, android.R.layout.simple_list_item_1, spinnerEvents)
        datesList.adapter = adapter

        return view
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
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // endregion Implements OnItemClickListener

    private class CustomDateEvent(val msg:String) : Event("Custom", "Custom", "") {

        override fun toString(): String {
            return msg
        }
    }
}
