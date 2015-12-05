package com.n8.intouch.addeventscreen


import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.n8.intouch.InTouchApplication

import com.n8.intouch.R
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.di.AddEventComponent
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddEventFragment : Fragment(), AddEventContract.View {

    var component: AddEventComponent? = null

    @Inject
    lateinit var contactUri:Uri

    @Inject
    lateinit var presenter:AddEventContract.UserInteractionListener

    @Inject
    lateinit var contentResolver:ContentResolver

    lateinit var progressBar:ContentLoadingProgressBar

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("AddEventComponent must be set")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        component?.inject(this)

        progressBar = ContentLoadingProgressBar(activity)

        presenter.onContactUriReceived(contactUri)

        return view
    }

    // region Implements AddEventView

    override fun displayContactInfo(contact: ContactLoader.Contact) {
        Toast.makeText(activity, contact.name, Toast.LENGTH_LONG).show();
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

    // endregion Implements AddEventView
}
