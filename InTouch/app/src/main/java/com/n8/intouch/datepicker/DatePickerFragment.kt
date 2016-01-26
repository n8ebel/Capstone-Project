package com.n8.intouch.datepicker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
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
    }

    val CUSTOM_DATE_SELECTION_INDEX = -1

    var component: DatePickerComponent? = null

    @Inject
    lateinit var contact:Contact

    @Inject
    lateinit var presenter: Contract.UserInteractionListener

    lateinit var continueButton: FloatingActionButton

    lateinit var customDateView:View

    lateinit var eventsRecyclerView: RecyclerView

    lateinit var eventsNoContentView:View

    var currentlySelectedPosition = -2

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("DatePickerComponent must be set")
        }

        component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView = inflater!!.inflate(R.layout.fragment_date_picker, container, false)

        customDateView = rootView!!.findViewById(R.id.custom_date_view)
        customDateView.setOnClickListener(View.OnClickListener {
            customDateView.isSelected = true

            var adapter = eventsRecyclerView.adapter
            if (adapter != null && adapter is DatesRecyclerAdapter) {
                adapter.clearSelected()
            }

            onCustomClicked()
        })

        eventsNoContentView = rootView!!.findViewById(R.id.dates_recyclerView_no_content_view)

        eventsRecyclerView = rootView!!.findViewById(R.id.dates_recyclerView) as RecyclerView
        eventsRecyclerView.layoutManager = LinearLayoutManager(context)

        continueButton = rootView.findViewById(R.id.continue_button) as FloatingActionButton
        continueButton.setOnClickListener(View.OnClickListener { view ->
            presenter.onContinueClicked()
        })

        presenter.onContactReceived(contact)

        return rootView
    }

    // region Implements Contract.View

    override fun bindEvents(events: List<Event>) {
        if (events.size == 0) {
            eventsNoContentView.visibility = View.VISIBLE
            eventsRecyclerView.visibility = View.GONE
            return
        }

        eventsRecyclerView.adapter = DatesRecyclerAdapter(events, object : DatesRecyclerAdapter.ClickListener {
            override fun onDateClicked(date: Event) {
                customDateView.isSelected = false
                onDateSelected(date)
            }
        })

        eventsNoContentView.visibility = View.GONE
        eventsRecyclerView.visibility = View.VISIBLE
    }

    override fun setContinueButtonVisible(visible: Boolean) {
        if (visible) continueButton.show() else continueButton.hide()
    }

    // endregion Implements Contract.View

    // region Implements DatePickerCard.DateClickedListener

    fun onDateSelected(event: Event) {
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

        public override fun toString(): String {
            return msg
        }
    }

    private class DateRecyclerViewHolder(view:View, clickLIstener:DateRecyclerViewHolder.ClickListener) : RecyclerView.ViewHolder(view) {

        public interface ClickListener {
            fun onClicked(position:Int)
        }

        val textView:TextView

        val onClickListener:View.OnClickListener = View.OnClickListener {
            clickLIstener.onClicked(adapterPosition)
        }

        init {
            textView = view.findViewById(R.id.text1) as TextView
            view.setOnClickListener(onClickListener)
        }
    }

    private class DatesRecyclerAdapter(val events: List<Event>, val clickListener:DatesRecyclerAdapter.ClickListener) : RecyclerView.Adapter<DateRecyclerViewHolder>(), DateRecyclerViewHolder.ClickListener {

        interface ClickListener {
            fun onDateClicked(date:Event)
        }

        var currentlySelectedItem = -1

        fun clearSelected(){
            currentlySelectedItem = -1
            notifyItemRangeChanged(0, itemCount)
        }

        fun setSelected(position: Int) {
            var oldSelected = currentlySelectedItem
            currentlySelectedItem = position
            notifyItemChanged(oldSelected)
            notifyItemChanged(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DateRecyclerViewHolder? {
            var view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item, parent, false)

            return DateRecyclerViewHolder(view, this)
        }

        override fun onBindViewHolder(holder: DateRecyclerViewHolder?, position: Int) {
            holder?.textView?.text = events[position].toString()
            if (position == currentlySelectedItem) {
                holder?.itemView?.isSelected = true
            } else {
                holder?.itemView?.isSelected = false
            }
        }

        override fun getItemCount(): Int {
            return events.size
        }

        // region Implements DateRecyclerViewHolder.ClickListener

        override fun onClicked(position: Int) {
            var oldSelected = currentlySelectedItem
            currentlySelectedItem = position
            clickListener.onDateClicked(events[position])
            notifyItemChanged(oldSelected)
            notifyItemChanged(position)
        }

        // endregion Implements DateRecyclerViewHolder.ClickListener

    }
}
