package com.n8.intouch.messageentryscreen

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.messageentryscreen.di.MessageEntryComponent
import javax.inject.Inject

class MessageEntryFragment : SwipeableFragment() , Contract.ViewListener, TextWatcher {

    interface Listener {
        fun onMessageEntered(message:String)
    }

    var component: MessageEntryComponent? = null

    @Inject
    lateinit var userInteractionListener:Contract.UserInteractionListener

    lateinit var messageEditText:EditText

    lateinit var continueButton:FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        component!!.inject(this)

        var rootView = inflater!!.inflate(R.layout.fragment_message_entry, container, false)

        messageEditText = rootView.findViewById(R.id.message_entry_editText) as EditText
        messageEditText.addTextChangedListener(this)

        continueButton = rootView.findViewById(R.id.continue_button) as FloatingActionButton
        continueButton.setOnClickListener(View.OnClickListener {
            userInteractionListener.onContinueClicked(messageEditText.text.toString())
        })

        return rootView
    }

    // region Implements Contract.ViewListener

    override fun setContinueButtonVisible(visible: Boolean) {
        if (visible) continueButton.show() else continueButton.hide()
    }

    // endregion Implements Contract.ViewListener

    // region Implements TextWatcher

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        userInteractionListener.onTextChanged(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // noop
    }

    override fun afterTextChanged(s: Editable?) {
        // noop
    }

    // endregion Implements TextWatcher
}