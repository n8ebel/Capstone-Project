package com.n8.intouch.browsescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.client.Firebase

import com.n8.intouch.R
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.model.User
import javax.inject.Inject

class BrowseFragment : Fragment(), BrowseContract.ViewController {

    var component: BrowseComponent? = null

    @Inject
    lateinit var firebase:Firebase

    @Inject
    lateinit var currentUser:User

    @Inject
    lateinit var presenter: BrowsePresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component?.inject(this) ?: throw IllegalStateException("BrowseComponent must be set")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Toast.makeText(context, currentUser.getUsername(), Toast.LENGTH_LONG).show()

        var view = inflater!!.inflate(R.layout.fragment_browse, container, false) as ViewGroup
        with(view){
            (findViewById(R.id.toolbar) as Toolbar).apply{
                title = getString(R.string.app_name)
            }
            findViewById(R.id.floating_action_button)!!.apply {
                setOnClickListener {
                    presenter.onAddPressed()
                }
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }
}
