package com.n8.intouch.repeatpicker

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    lateinit var component: RepeatPickerComponent

    @Inject
    lateinit var userInteractionLister: Contract.UserInteractionListener

    lateinit var continueButton:FloatingActionButton

    lateinit var timeTextView:TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component.inject(this)

        var rootView = inflater!!.inflate(R.layout.repeat_picker_card, container, false)

        var view = rootView.findViewById(R.id.time_picker_container)
        view.setOnClickListener(View.OnClickListener {
            userInteractionLister.onDateSelectorClicked()
        })

        timeTextView = rootView.findViewById(R.id.time_textView) as TextView

        continueButton = rootView.findViewById(R.id.continue_button) as FloatingActionButton
        continueButton.setOnClickListener(View.OnClickListener {
            userInteractionLister.onContinueClicked()
        })

        with(rootView.findViewById(R.id.repeat_picker_frequency_editText) as EditText){
            addTextChangedListener(object : TextWatcher{
                override fun onTextChanged(changedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (changedText == null || changedText.length == 0) {
                        userInteractionLister.onFrequencySelected(0)
                    }else{
                        val frequency = changedText.toString().toInt()
                        userInteractionLister.onFrequencySelected(frequency)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // noop
                }

                override fun afterTextChanged(p0: Editable?) {
                    // noop
                }

            })

            setText(Contract.UserInteractionListener.DEFAULT_FREQUENCY)
        }


        val arrayDisplay = arrayOf(
            getString(R.string.days), getString(R.string.weeks), getString(R.string.years)
        )

        val arrayValues = arrayOf(
                DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.YEAR_IN_MILLIS
        )

        with(rootView.findViewById(R.id.repeat_picker_interval_spinner) as Spinner){
            adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayDisplay)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // noop
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    userInteractionLister.onIntervalSelected(arrayValues[position])
                }

            }
        }

        userInteractionLister.onViewCreated()

        return rootView
    }

    // region Implements Contract.View

    override fun setContinueButtonVisible(visible: Boolean) {
        if (visible) continueButton.show() else continueButton.hide()
    }

    override fun setTimeText(text:String) {
        timeTextView.text = text
    }

    // endregion Implements Contract.View
}
