package com.n8.intouch.signin

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import com.n8.intouch.R
import com.n8.intouch.common.BaseActivity

class SignInActivity : BaseActivity(), View.OnLayoutChangeListener {

    lateinit var rootContentView:ViewGroup

    lateinit var credentialEntryView:View

    lateinit var addAccountView:View

    lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_sign_in)
        rootContentView = findViewById(R.id.content_container) as ViewGroup

        addAccountView = layoutInflater!!.inflate(R.layout.fragment_add_account, rootContentView, false)
        credentialEntryView = layoutInflater.inflate(R.layout.fragment_credential_entry, rootContentView, false)

        addAccountView.visibility = View.INVISIBLE
        credentialEntryView.visibility = View.INVISIBLE

        rootContentView.addView(credentialEntryView)
        rootContentView.addView(addAccountView)

        rootContentView.addOnLayoutChangeListener(this)

        fab = credentialEntryView.findViewById(R.id.floating_action_button) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener {
            goo()
        })
    }

    // region Implements OnLayoutChangeListener

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        rootContentView.removeOnLayoutChangeListener(this)

        revealView(credentialEntryView, rootContentView)
    }

    // region Implements OnLayoutChangeListener

    private fun revealView(view:View, parent:View){
        // get the center for the clipping circle
        var cx = parent.width / 2;
        var cy = parent.height / 2;

        // get the final radius for the clipping circle
        var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat()).setDuration(700)

        // make the view visible and start the animation
        view.visibility = View.VISIBLE

        anim.start()
    }

    private fun getFinalRevealRadius(view:View) : Double {
        var cx = view.width / 2;
        var cy = view.height / 2;

        // get the final radius for the clipping circle
        return Math.hypot(cx.toDouble(), cy.toDouble());
    }

    private fun goo() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionEnd(transition: Transition?) {
                val addAccountView = layoutInflater.inflate(R.layout.fragment_add_account, rootContentView, true)

                ViewAnimationUtils.createCircularReveal(
                        addAccountView,
                        (addAccountView.width / 2).toInt(),
                        (addAccountView.height / 2).toInt(),
                        0f,
                        getFinalRevealRadius(addAccountView).toFloat()
                ).setDuration(1500).start()
            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {

            }
        })

        TransitionManager.beginDelayedTransition(credentialEntryView as ViewGroup, transition);
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER
        fab.layoutParams = layoutParams
    }
}