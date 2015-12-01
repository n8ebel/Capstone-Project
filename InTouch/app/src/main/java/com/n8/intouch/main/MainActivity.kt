package com.n8.intouch.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.widget.Toast

import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import com.n8.intouch.browsescreen.TabbedFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {
    val TAG = "MainActivity"

    @Inject
    lateinit var presenter: MainPresenter

    val component = createComponent()

    lateinit var progressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        setContentView(R.layout.activity_main)

        // Only add the tabbed fragment the first time the activity is created
        //
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().
                    add(R.id.fragmentContainer, TabbedFragment(), null).
                    addToBackStack("foo").
                    commit()
        }
    }

    // region Implements MainView

    override fun showProgress() {
        Log.d(TAG, "showing progress")
        progressBar.show()
    }

    override fun hideProgress() {
        Log.d(TAG, "hide progress")
        progressBar.hide()
    }

    override fun showResult(result: String) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }

    // endregion Implements MainView

    private fun createComponent(): MainComponent {
        return DaggerMainComponent.builder()
                .applicationComponent(InTouchApplication.graph)
                .mainModule(MainModule(this, supportFragmentManager))
                .build()
    }
}
