package com.n8.intouch.browsescreen


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.n8.intouch.application.InTouchApplication

import com.n8.intouch.R
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.common.TabbedFragment
import javax.inject.Inject

class BrowseFragment : TabbedFragment(), BrowseContract.View {

    var component: BrowseComponent? = null

    @Inject
    lateinit var presenter:TabbedFragmentPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("BrowseComponent must be set")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        component?.inject(this)

        var view = inflater!!.inflate(R.layout.fragment_browse, container, false) as ViewGroup

        // Get base implementation layout and add to container
        //
        var baseView = super.onCreateView(inflater, container, savedInstanceState)
        view.addView(baseView, 0)

        fab = view.findViewById(R.id.fab) as FloatingActionButton

        toolbar.title = getString(R.string.app_name)


        // TODO Fix this
        fab.setOnClickListener {
            presenter.onAddPressed()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }

    // region Implements TabbedFragmentView

    override fun displayError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }

    // region Implements TabbedFragmentView

    override fun createFragmentList(): List<Fragment>{
        return listOf<Fragment>(EventsListFragment(), ContactsFragment())
    }
}
