package com.n8.intouch.addeventscreen


import android.content.DialogInterface
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
import com.n8.intouch.common.BaseComponent
import com.n8.intouch.common.BaseFragment
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.messageentryscreen.di.DaggerMessageEntryComponent
import com.n8.intouch.messageentryscreen.di.MessageEntryModule
import com.n8.intouch.model.Contact
import com.n8.intouch.messageentryscreen.MessageEntryFragment
import com.n8.intouch.repeatpicker.RepeatPickerFragment
import com.n8.intouch.setupBackNavigation
import java.util.*
import javax.inject.Inject

/**
 * Fragment that allows a user to create a new scheduled event for a contact.
 */
class AddEventFragment : BaseFragment(), AddEventContract.ViewController {

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

    // region Implements AddEventContract.ViewController

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
        mCardStack?.addView(fragment, tag, swipeable)
    }

    override fun promptToConfirmScheduledEvent(title: String, message: String) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Schedule", DialogInterface.OnClickListener { dialogInterface, i ->
                presenter.scheduleEvent()
            })
            setNeutralButton("Cancel", null)

            create().show()
        }
    }

    // endregion Implements AddEventContract.ViewController
}
