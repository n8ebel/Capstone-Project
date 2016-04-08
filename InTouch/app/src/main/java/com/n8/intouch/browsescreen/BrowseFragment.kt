package com.n8.intouch.browsescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.n8.intouch.R
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.common.BaseFragment
import com.n8.intouch.model.ScheduledEvent
import javax.inject.Inject

class BrowseFragment : BaseFragment(), BrowseContract.ViewController {

    var component: BrowseComponent? = null

    @Inject
    lateinit var presenter: BrowsePresenter

    lateinit var eventsRecyclerView:RecyclerView

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

    override fun displayEvents(events: List<ScheduledEvent>) {
        eventsRecyclerView.adapter = ScheduledEventsRecyclerAdapter(events)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }

    // endregion Implements BrowseContract.ViewController

    class ScheduledEventViewHolder(val view:View) : RecyclerView.ViewHolder(view) {

        val firstLineText:TextView

        init{
            firstLineText = view.findViewById(R.id.text1) as TextView
        }

        fun bindScheduledEvent(event:ScheduledEvent) {
            firstLineText.text = event.scheduledMessage
        }

    }

    class ScheduledEventsRecyclerAdapter(private val scheduledEvents:List<ScheduledEvent>) : RecyclerView.Adapter<ScheduledEventViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ScheduledEventViewHolder? {
            val view = LayoutInflater.from(p0?.context).inflate(R.layout.scheduled_event_row_item, p0, false)
            return ScheduledEventViewHolder(view)
        }

        override fun onBindViewHolder(holder: ScheduledEventViewHolder?, position: Int) {
            holder?.bindScheduledEvent(scheduledEvents[position])
        }

        override fun getItemCount(): Int {
            return scheduledEvents.size
        }

    }
}
