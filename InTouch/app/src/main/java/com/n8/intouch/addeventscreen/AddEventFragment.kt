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

    lateinit var rootView:View

    lateinit var progressBar:ContentLoadingProgressBar

    lateinit var collapsingToolbar:CollapsingToolbarLayout

    lateinit var contactThumbnailPlaceholder:ImageView

    lateinit var contactThumbnailImageView:ImageView

    lateinit var spinner:Spinner

    lateinit var headerContainer:ViewGroup

    lateinit var contentContainer:ViewGroup

    lateinit var startHeader: StartHeader

    lateinit var datePickerCard: DatePickerCard

    lateinit var datesList: ListView

    lateinit var adapter:ArrayAdapter<Event>

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
        rootView = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        collapsingToolbar = rootView.findViewById(R.id.collapsingToolbar) as CollapsingToolbarLayout
        collapsingToolbar.isTitleEnabled = true

        var toolbar = rootView.findViewById(R.id.toolbar) as Toolbar
        toolbar.setupBackNavigation { presenter.onNavIconPressed() }

        contactThumbnailPlaceholder = rootView.findViewById(R.id.contactThumbnailPlaceholder) as ImageView
        contactThumbnailImageView = rootView.findViewById(R.id.contactThumbnail) as ImageView

        headerContainer = rootView.findViewById(R.id.headerContainer) as ViewGroup
        contentContainer = rootView.findViewById(R.id.contentContainer) as ViewGroup

        contentContainer.layoutTransition = createContentConatinerLayoutTransition()

        startHeader = inflater.inflate(R.layout.add_event_header_start, contentContainer, false) as StartHeader

        datePickerCard = inflater.inflate(R.layout.date_picker_card, contentContainer, false) as DatePickerCard
        var datePickerFAB = datePickerCard.findViewById(R.id.continueFAB) as FloatingActionButton
        datePickerFAB.setOnClickListener { presenter.onContinueWithDateSelected() }
        datesList = datePickerCard.findViewById(R.id.listView) as ListView
        datesList.onItemClickListener = this

        progressBar = ContentLoadingProgressBar(activity)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        presenter.onContactUriReceived(contactUri)
    }

    override fun onResume() {
        super.onResume()

        // Add in onResume to ensure user sees the animation
        if (!startHeader.isAttachedToWindow) {
            var handler = Handler()
            handler.postDelayed(
                    {
                        headerContainer.addView(startHeader)
                        contentContainer.addView(datePickerCard)
                    }
                    , 100)
        }
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

        collapsingToolbar.title = contact.name

        if (contact.thumbnail != null) {
            var roundedThumbnail = RoundedBitmapDrawableFactory.create(activity.resources, contact.thumbnail)
            contactThumbnailImageView.setImageDrawable(roundedThumbnail)
            contactThumbnailPlaceholder.visibility = View.GONE
            contactThumbnailImageView.visibility = View.VISIBLE
        }

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

    override fun displayRepeatPicker() {
        var repeatPicker = activity.layoutInflater.inflate(R.layout.repeat_picker_card, contentContainer, false) as RepeatPickerCard
        var repeatHeader = activity.layoutInflater.inflate(R.layout.add_event_header_repeat, headerContainer, false) as RepeatHeader

        var coveredTransitionAnimator = AnimatorInflater.loadAnimator(context, R.animator.covered_transition)
        coveredTransitionAnimator.setTarget(datePickerCard)
        coveredTransitionAnimator.start()

        datePickerCard.cardElevation = datePickerCard.cardElevation * .8f

        val startPoint = Point()
        repeatPicker.setOnTouchListener({ view, event ->
            try {
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    startPoint.x = event.rawX.toInt()
                    startPoint.y = event.rawY.toInt()
                    Log.d("foo", "actionDown")
                }else if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                    Log.d("foo", "actionMove  x: ${event.rawX} y:${event.rawY} ")
                    var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    var windowSize = Point()
                    windowManager.defaultDisplay.getSize(windowSize)

                    var screenWidth = windowSize.x


                    repeatPicker.translationX = event.rawX - startPoint.x
                    repeatPicker.translationY = event.rawY - startPoint.y
                    repeatPicker.rotation++
                }else if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    Log.d("foo", "actionEnd")
                    repeatPicker.animate().translationX(0f).translationY(0f).setDuration(300).start()
                }
            } catch(e: Exception){

            }

            true
        })

        repeatPicker.setOnDragListener({view, event ->
            Log.d("foo", "goo")
            true
        })

        headerContainer.addView(repeatHeader)
        contentContainer.addView(repeatPicker)
    }

    // endregion Implements AddEventView

    private fun createContentConatinerLayoutTransition() : LayoutTransition {
        var layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)

        var duration = resources.getInteger(android.R.integer.config_longAnimTime)
        layoutTransition.setDuration(LayoutTransition.APPEARING, duration.toLong())
        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 0)

        var addTransitionAnimator = AnimatorInflater.loadAnimator(context, R.animator.add_transition)
        layoutTransition.setAnimator(LayoutTransition.APPEARING, addTransitionAnimator)

        var coveredTransitionAnimator = AnimatorInflater.loadAnimator(context, R.animator.covered_transition)
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, coveredTransitionAnimator)

        return layoutTransition
    }

    private class CustomDateEvent(val msg:String) : Event("Custom", "Custom", "") {

        override fun toString(): String {
            return msg
        }
    }
}
