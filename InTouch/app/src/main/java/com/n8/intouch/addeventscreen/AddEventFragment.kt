package com.n8.intouch.addeventscreen


import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.n8.intouch.InTouchApplication

import com.n8.intouch.R
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddEventFragment : Fragment(), AddEventView {

    val component = createComponent()

    @Inject
    lateinit var presenter:AddEventPresenter

    @Inject
    lateinit var contentResolver:ContentResolver

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        component.inject(this)

        return view
    }

    // region Implements AddEventView

    override fun displayContactInfo(contactInfo: String) {
        Toast.makeText(activity, contactInfo, Toast.LENGTH_LONG).show();
    }

    override fun showProgress() {
        throw UnsupportedOperationException()
    }

    override fun hideProgress() {
        throw UnsupportedOperationException()
    }

    override fun displayError(error: Throwable) {
        throw UnsupportedOperationException()
    }

    // endregion Implements AddEventView

    private fun createComponent() : AddEventComponent {
        return DaggerAddEventComponent.builder().addEventModule(AddEventModule(this)).applicationComponent(InTouchApplication.graph).build()
    }
}
