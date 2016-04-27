package com.n8.intouch.browsescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast

import com.n8.intouch.R
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.common.BaseFragment
import com.n8.intouch.common.SchedulingUtils
import com.n8.intouch.getQuantityString
import com.n8.intouch.getString
import com.n8.intouch.model.ScheduledEvent
import javax.inject.Inject

class BrowseFragment : BaseFragment(), BrowseContract.ViewController {

    var component: BrowseComponent? = null

    @Inject
    lateinit var presenter: BrowsePresenter

    lateinit var eventsRecyclerView:RecyclerView

    lateinit var noContentView:View

    lateinit var mProgressView:View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component?.inject(this) ?: throw IllegalStateException("BrowseComponent must be set")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater!!.inflate(R.layout.fragment_browse, container, false) as ViewGroup
        with(view){
            findViewById(R.id.floating_action_button)!!.apply {
                setOnClickListener {
                    presenter.onAddPressed()
                }
            }

            eventsRecyclerView = findViewById(R.id.browse_content) as RecyclerView
            eventsRecyclerView.layoutManager = LinearLayoutManager(context)

            noContentView = findViewById(R.id.browse_no_content)

            mProgressView = findViewById(R.id.browse_progress_layout)
        }

        if (activity.intent?.getBooleanExtra(BrowseActivity.EXTRA_AUTO_ADD, false) ?: false){
            presenter.onAddPressed()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }

    // region Implements BrowseContract.ViewController

    override fun showProgress() {
        mProgressView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgressView.visibility = View.GONE
    }

    override fun showNoContentView() {
        noContentView.visibility = View.VISIBLE
    }

    override fun hideNoContentView() {
        noContentView.visibility = View.GONE
    }

    override fun displayEvents(events: List<ScheduledEvent>) {
        val adapterList:MutableList<ScheduledEvent> = mutableListOf()
        events.forEach { adapterList.add(it) }

        eventsRecyclerView.adapter = ScheduledEventsRecyclerAdapter(adapterList,
                { event -> presenter.onListItemClicked(event)},
                { event, view -> presenter.onListItemOverflowClicked(event, view) }
        )
    }

    override fun displayAddedEvent(event: ScheduledEvent, index: Int) {
        with(eventsRecyclerView.adapter as ScheduledEventsRecyclerAdapter){
            scheduledEvents.add(index, event)
            notifyItemInserted(index)
        }
    }

    override fun hideRemovedEvent(event: ScheduledEvent, index: Int) {
        with(eventsRecyclerView.adapter as ScheduledEventsRecyclerAdapter){
            scheduledEvents.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }

    override fun displayError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun promptToRemoveEvent(event: ScheduledEvent) {
        AlertDialog.Builder(context).apply {
            setTitle(R.string.remove_event)
            setMessage(R.string.confirm_remove_event)
            setNeutralButton(android.R.string.cancel, null)
            setPositiveButton(android.R.string.ok, { dialog, which ->
                presenter.onRemoveEventConfirmed(event)
            })
            show()
        }
    }

    override fun showListItemOverflowMenu(event:ScheduledEvent, anchorView:View) {
        PopupMenu(context, anchorView).apply {
            inflate(R.menu.browse_list_item_overflow_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove_item -> {
                        presenter.onRemoveEventClicked(event)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    // endregion Implements BrowseContract.ViewController

    class ScheduledEventViewHolder(val view:View) : RecyclerView.ViewHolder(view) {

        val messageTextView:TextView
        val phoneNumberTextView:TextView
        val repeatScheduleTextView:TextView
        val overflowImageView:ImageView

        init{
            messageTextView = view.findViewById(R.id.message_textView) as TextView
            phoneNumberTextView = view.findViewById(R.id.phone_number_textView) as TextView
            repeatScheduleTextView = view.findViewById(R.id.repeat_schedule_textView) as TextView
            overflowImageView = view.findViewById(R.id.overflow_imageView) as ImageView
        }

        fun bindScheduledEvent(event:ScheduledEvent) {
            messageTextView.text = event.scheduledMessage

            phoneNumberTextView.text = event.phoneNumber

            repeatScheduleTextView.text = view.getString(R.string.every) + " " +
                    view.getQuantityString( SchedulingUtils.getPluralsStringIdForDuration(view.context, event), event.repeatInterval) + " " +
                    view.getString(R.string.at) + " " + SchedulingUtils.getTimeDisplayString(event.startDateHour, event.startDateMin)
        }

    }

    class ScheduledEventsRecyclerAdapter(val scheduledEvents:MutableList<ScheduledEvent>,
                                         private val eventClickListener: (ScheduledEvent) -> Unit,
                                         private val overflowClickListener: (ScheduledEvent, View) -> Unit
                                         ) : RecyclerView.Adapter<ScheduledEventViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ScheduledEventViewHolder? {
            val view = LayoutInflater.from(p0?.context).inflate(R.layout.scheduled_event_row_item, p0, false)
            return ScheduledEventViewHolder(view).apply {
                itemView.setOnClickListener { eventClickListener(scheduledEvents[adapterPosition]) }
                overflowImageView.setOnClickListener { overflowClickListener(scheduledEvents[adapterPosition], overflowImageView) }
            }
        }

        override fun onBindViewHolder(holder: ScheduledEventViewHolder?, position: Int) {
            holder?.bindScheduledEvent(scheduledEvents[position])
        }

        override fun getItemCount(): Int {
            return scheduledEvents.size
        }

    }
}
