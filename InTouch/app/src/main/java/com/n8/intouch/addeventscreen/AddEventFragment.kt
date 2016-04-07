package com.n8.intouch.addeventscreen


import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.n8.intouch.R
import com.n8.intouch.addeventscreen.di.AddEventComponent
import com.n8.intouch.common.BackPressedListener
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.messageentryscreen.di.DaggerMessageEntryComponent
import com.n8.intouch.messageentryscreen.di.MessageEntryModule
import com.n8.intouch.model.Contact
import com.n8.intouch.messageentryscreen.MessageEntryFragment
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import com.n8.intouch.setupBackNavigation
import javax.inject.Inject

/**
 * Fragment that allows a user to create a new scheduled event for a contact.
 */
class AddEventFragment : Fragment(), BackPressedListener, AddEventContract.ViewController,
        RepeatPickerFragment.Listener, MessageEntryFragment.Listener {


    lateinit var component: AddEventComponent

    @Inject
    lateinit var contactUri:Uri

    @Inject
    lateinit var presenter:AddEventContract.UserInteractionListener

    var mProgressBar:ContentLoadingProgressBar? = null

    var mCollapsingToolbar:CollapsingToolbarLayout? = null

    var mContactThumbnailPlaceholder:ImageView? = null

    var mContactThumbnailImageView:ImageView? = null

    var mHeaderTextView:TextView? = null

    var mCardStack:CardStack? = null

//    var startDateTimestamp = -1L
//
//    var startDateHour = -1
//
//    var startDateMin = -1
//
//    var repeatInterval = -1  // value such as '1' or '3'
//
//    var repeatDuration = -1L  // value such as week.inMillis()
//
//    var scheduledMessage = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        component.inject(this)

        return (inflater!!.inflate(R.layout.fragment_add_for_date, container, false) as ViewGroup).apply {
            mCollapsingToolbar = (findViewById(R.id.collapsingToolbar) as CollapsingToolbarLayout).apply {
                isTitleEnabled = true
            }

            var toolbar = findViewById(R.id.toolbar) as Toolbar
            toolbar.setupBackNavigation { presenter.onNavIconPressed() }

            mContactThumbnailPlaceholder = findViewById(R.id.contactThumbnailPlaceholder) as ImageView
            mContactThumbnailImageView = findViewById(R.id.contactThumbnail) as ImageView

            mProgressBar = ContentLoadingProgressBar(activity)

            mCardStack = CardStack(childFragmentManager, this, R.id.content_container, R.id.fragment_container)

            mHeaderTextView = rootView.findViewById(R.id.header_textView) as TextView

            presenter.start()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onContactUriReceived(contactUri)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.stop()
    }

    // region Implements BackPressedListener

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }

        return false
    }

    // endregion Implements BackPressedListener

    // region Implements AddEventView

    override fun displayContactInfo(contact: Contact) {

        mCollapsingToolbar?.title = contact.name

        if (contact.thumbnail != null) {
            var roundedThumbnail = RoundedBitmapDrawableFactory.create(activity.resources, contact.thumbnail)
            mContactThumbnailImageView?.setImageDrawable(roundedThumbnail)
            mContactThumbnailPlaceholder?.visibility = View.GONE
            mContactThumbnailImageView?.visibility = View.VISIBLE
        }
    }

    override fun showProgress() {
        mProgressBar?.show()
    }

    override fun hideProgress() {
        mProgressBar?.hide()
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
    }

    override fun finish() {
        activity.finish()
    }

    override  fun setHeaderText(text: String?) {
        mHeaderTextView?.text = text
    }

    override fun showSwipeableFragment(fragment:SwipeableFragment, tag:String, swipeable:Boolean){
        addViewToStack(fragment, tag, swipeable)
    }

    // endregion Implements AddEventView

    // region Implements RepeatPickerFragment.Listener

    override fun onRepeatScheduleSelected(hour: Int, min: Int, interval: Int, duration: Long) {
//        startDateHour = hour
//        startDateMin = min
//        repeatInterval = interval
//        repeatDuration = duration
//        showMessageEntry()
    }

    // endregion Implements RepeatPickerFragment.Listener

    // region Implements MessageEntryFragment.Listener

    override fun onMessageEntered(message: String) {

//        scheduledMessage = message
//
//        var builder = AlertDialog.Builder(context)
//        builder.setTitle("Schedule repeated message")
//        builder.setMessage("Starting:  ${DATE_FORMAT.format(Date(startDateTimestamp))} \n" +
//                "Repating every $repeatInterval ${displayUnitsForRepeatDuration(repeatDuration)} \n" +
//                "at $startDateHour:$startDateMin with message: \n" + scheduledMessage)
//        builder.setPositiveButton("Schedule", DialogInterface.OnClickListener { dialogInterface, i ->
//            throw NotImplementedError()
//        })
//        builder.setNeutralButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
//
//        })
//        builder.create().show()
    }

    // endregion Implements MessageEntryFragment.Listener

    // region Private Methods

    private fun addViewToStack(fragment: SwipeableFragment, tag: String, swipeable: Boolean) {
        mCardStack?.addView(fragment, tag, swipeable)
    }

    private fun showMessageEntry() {
        var fragment = MessageEntryFragment()

        var component = DaggerMessageEntryComponent.builder().
                messageEntryModule(MessageEntryModule(fragment, this)).
                build()
        fragment.component = component

        addViewToStack(fragment, "MessageEntry", true)
    }

    private fun displayUnitsForRepeatDuration(duration:Long) : String {
        return "weeks"
    }

    // endregion Private Methods
}
