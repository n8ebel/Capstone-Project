package com.n8.intouch.signin

import android.animation.Animator
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.n8.intouch.R
import com.n8.intouch.common.BackPressedListener

/**
 *  Handles entry of email/password
 */
class AddAccountFragment : Fragment(), View.OnLayoutChangeListener, BackPressedListener {

    lateinit var rootView:View

    lateinit var fab:FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_add_account, container, false)
        rootView.visibility = View.INVISIBLE
        rootView.addOnLayoutChangeListener(this)

        return rootView
    }

    // region Implements View.OnLayoutChangeListener

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        view.removeOnLayoutChangeListener(this)

        // get the center for the clipping circle
        var cx = rootView.getWidth() / 2;
        var cy = rootView.getHeight() / 2;

        // get the final radius for the clipping circle
        var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

        // create the animator for this view (the start radius is zero)
        var anim =ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0f, finalRadius.toFloat());

        anim.setDuration(1500)

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        rootView.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
        anim.start();

    }

    // endregion Implements View.OnLayoutChangeListener

    // region Implements BackPressedListener

    override fun onBackPressed(): Boolean {
        var cx = rootView.getWidth() / 2;
        var cy = rootView.getHeight() / 2;

        // get the final radius for the clipping circle
        var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

        // create the animator for this view (the start radius is zero)
        var anim =ViewAnimationUtils.createCircularReveal(rootView, cx, cy, finalRadius.toFloat(), 0f);

        anim.setDuration(1500)

        exitTransition = anim

        return false
    }

    // endregion Implements BackPressedListener
}