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
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.n8.intouch.R
import com.n8.intouch.common.BackPressedListener

/**
 *  Handles entry of email/password
 */
class CredentialEntryFragment : Fragment(), View.OnLayoutChangeListener, BackPressedListener {

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
            val transition = TransitionInflater.from(context).inflateTransition(R.transition.changebounds_with_arcmotion)
            transition.addListener(object: Transition.TransitionListener{
                override fun onTransitionCancel(transition: Transition?) {

                }

                override fun onTransitionEnd(transition: Transition?) {
                    val addAccountView = inflater.inflate(R.layout.fragment_add_account, container, true)

                    ViewAnimationUtils.createCircularReveal(addAccountView, (rootView.width/2).toInt(), (rootView.height/2).toInt(), 0f, getFinalRadius().toFloat()).setDuration(1500).start()
                }

                override fun onTransitionPause(transition: Transition?) {

                }

                override fun onTransitionResume(transition: Transition?) {

                }

                override fun onTransitionStart(transition: Transition?) {

                }
            })

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition);
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER
            fab.layoutParams = layoutParams

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
        var anim =ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0f, finalRadius.toFloat()).setDuration(700);

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        anim.start();

    }

    override fun onBackPressed(): Boolean {
        val transition = TransitionInflater.from(context).inflateTransition(R.transition.changebounds_with_arcmotion)
        transition.addListener(object: Transition.TransitionListener{
            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionEnd(transition: Transition?) {

            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {
                //val addAccountView = inflater.inflate(R.layout.fragment_add_account, container, true)

                //ViewAnimationUtils.createCircularReveal(addAccountView, (rootView.width/2).toInt(), (rootView.height/2).toInt(), 0f, getFinalRadius().toFloat()).setDuration(1500).start()
            }
        })

        TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition);
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER
        fab.layoutParams = layoutParams

        return true
    }

    // endregion Implements View.OnLayoutChangeListener

    private fun getFinalRadius() : Double{
        // get the center for the clipping circle
        var cx = rootView.getWidth() / 2;
        var cy = rootView.getHeight() / 2;

        // get the final radius for the clipping circle
        return Math.hypot(cx.toDouble(), cy.toDouble());
    }
}