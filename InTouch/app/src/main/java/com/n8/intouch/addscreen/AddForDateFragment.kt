package com.n8.intouch.addscreen


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.n8.intouch.R

/**
 * A simple [Fragment] subclass.
 */
class AddForDateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_add_for_date, container, false)

        var pickContactFab = view.findViewById(R.id.fab) as FloatingActionButton
        pickContactFab.setOnClickListener {
            pickContact()
        }

        return view
    }

    private fun pickContact() {
        var pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, 1);
    }

}
