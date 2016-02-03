package com.n8.intouch.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils


class ViewUtils {
    companion object {

        public fun revealView(view: View){
            revealView(view, null)
        }

        public fun revealView(view: View, listener:Animator.AnimatorListener?){
            // get the center for the clipping circle
            var cx = view.width / 2;
            var cy = view.height / 2;

            // get the final radius for the clipping circle
            var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat()).setDuration(700)

            if (listener != null) {
                anim.addListener(listener)
            }

            // make the view visible and start the animation
            view.visibility = View.VISIBLE

            anim.start()
        }

        public fun hideView(view: View){
            hideView(view, null)
        }

        public fun hideView(view: View, listener: Animator.AnimatorListener?){
            // get the center for the clipping circle
            var cx = view.width / 2;
            var cy = view.height / 2;

            // get the final radius for the clipping circle
            var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius.toFloat(), 0f).setDuration(700)

            anim.addListener(object:AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })

            if (listener != null) {
                anim.addListener(listener)
            }

            anim.start()
        }
    }
}