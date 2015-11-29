package com.n8.intouch.addscreen


import android.os.Bundle
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
        return inflater!!.inflate(R.layout.fragment_add_for_date, container, false)
    }

}// Required empty public constructor
