package com.n8.intouch.signin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.n8.intouch.R

/**
 *  Handles entry of email/password
 */
class CredentialEntryFragment : Fragment(), View.OnLayoutChangeListener {

    public interface Listener {
        fun addAccountClicked()
    }

    lateinit var rootView:View

    lateinit var fab:FloatingActionButton

    var listener:Listener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_credential_entry, container, false)
        rootView.visibility = View.INVISIBLE
        rootView.addOnLayoutChangeListener(this)

        fab = rootView.findViewById(R.id.floating_action_button) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener {
            val cx = rootView.width / 2 - (fab.x + fab.width/2)
            val cy = rootView.height /2 - (fab.y + fab.height/2)

            val xAnim = ObjectAnimator.ofFloat(fab, "translationX", cx.toFloat())
            val yAnim = ObjectAnimator.ofFloat(fab, "translationY", cy.toFloat())

            xAnim.setDuration(500).interpolator = FastOutLinearInInterpolator()
            yAnim.setDuration(500).interpolator = LinearOutSlowInInterpolator()

            val animSet = AnimatorSet()
            animSet.playTogether(xAnim, yAnim)
            animSet.addListener(object:AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    listener?.addAccountClicked()
                }
            })
            animSet.start()
        })

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

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        anim.start();

    }

    // endregion Implements View.OnLayoutChangeListener
}