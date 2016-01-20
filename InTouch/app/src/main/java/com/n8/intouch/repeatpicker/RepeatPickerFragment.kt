package com.n8.intouch.repeatpicker

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment
import com.n8.intouch.repeatpicker.di.RepeatPickerComponent
import javax.inject.Inject

/**
 * Allows use to pick a repeat schedule for an event
 */
class RepeatPickerFragment : SwipeableFragment(), Contract.View {

    interface Listener {
        fun onRepeatScheduleSelected(startHour:Int, startMin:Int, interval:Int, duration:Long)
    }

    var component: RepeatPickerComponent? = null

    @Inject
    lateinit var userInteractionLister: Contract.UserInteractionListener

    lateinit var continueButton:FloatingActionButton

    lateinit var timeTextView:TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (component == null) {
            throw IllegalStateException("RepeatPickerComponenet must be set")
        }

        component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater!!.inflate(R.layout.repeat_picker_card, container, false)

        var view = rootView.findViewById(R.id.time_picker_container)
        view.setOnClickListener(View.OnClickListener {
            userInteractionLister.onDateSelectorClicked()
        })

        continueButton = rootView.findViewById(R.id.continue_button) as FloatingActionButton
        continueButton.setOnClickListener(View.OnClickListener {
            userInteractionLister.onContinueClicked()
        })

        timeTextView = rootView.findViewById(R.id.time_textView) as TextView

        userInteractionLister.onViewCreated()

        return rootView
    }

    // region Implements Contract.View

    override fun setContinueButtonVisible(visible: Boolean) {
        continueButton.show()
    }

    override fun setTimeText(text:String) {
        timeTextView.text = text
    }

    // endregion Implements Contract.View
}
