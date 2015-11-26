package com.n8.intouch.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.widget.Toast

import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    val TAG = "MainActivity"

    companion object {
        @JvmStatic public lateinit var graph: MainComponent
    }

    @Inject
    lateinit var presenter: MainPresenter

    lateinit var progressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        graph = DaggerMainComponent.builder()
                .applicationComponent(InTouchApplication.graph)
                .mainModule(MainModule(this))
                .build()

        graph.inject(this)

        setContentView(R.layout.activity_main)

        progressBar = ContentLoadingProgressBar(this)

        var button = findViewById(R.id.theButton)
        button.setOnClickListener {
            presenter.showFirebaseToast()
        }

        var contentProviderButton = findViewById(R.id.contentProviderButton)
        contentProviderButton.setOnClickListener {
            presenter.showData("selection args go here", arrayOf("foo", "goo"))
        }

        var phoneNumberInputLayout = findViewById(R.id.phoneNumber_textInputLayout) as TextInputLayout
        var scheduleTextButton = findViewById(R.id.scheduleText_button)
        scheduleTextButton.setOnClickListener {
            presenter.scheduleText(phoneNumberInputLayout.editText.text.toString())
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
}
