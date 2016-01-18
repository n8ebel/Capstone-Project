package com.n8.intouch.addeventscreen

import android.animation.AnimatorInflater
import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Point
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.n8.intouch.R
import com.n8.intouch.common.SwipeableFragment

class CardStack(val fragmentManager:FragmentManager ,view: View, val rootId:Int, val contentId:Int) {

    val ANIMATION_DURATION = view.context.resources.getInteger(android.R.integer.config_longAnimTime)
    val addTransitionAnimator = AnimatorInflater.loadAnimator(view.context, R.animator.add_transition)
    val coveredTransitionAnimator = AnimatorInflater.loadAnimator(view.context, R.animator.covered_transition)
    var uncoveredTransitionAnimator = AnimatorInflater.loadAnimator(view.context, R.animator.uncovered_transition)

    val container: FrameLayout = view.findViewById(rootId) as FrameLayout

    val contentContainer: ViewGroup = view.findViewById(contentId) as ViewGroup

    val screenWidth:Int

    val screenHeight:Int

    val leftClamp: Double

    val rightClamp: Double

    val topClamp: Double

    val bottomClamp: Double

    init {
        contentContainer.layoutTransition = createContentConatinerLayoutTransition()

        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)

        screenWidth = screenSize.x
        screenHeight = screenSize.y

        leftClamp = screenWidth * .2
        rightClamp = screenWidth * .8

        topClamp = container.y + (container.height * .20)
        bottomClamp = screenHeight * .8
    }

    // region Public Functions

    public fun addView(@NonNull fragment: SwipeableFragment, tag:String, swipeable:Boolean) {
        fragmentManager.
                beginTransaction().
                add(contentId, fragment).
                addToBackStack(tag).
                commit()

        if (swipeable) {
            addTouchListener(fragment)
        }

        var index = 0
        while (index < fragmentManager.backStackEntryCount) {
            val viewBeingCovered = contentContainer.getChildAt(index)
            viewBeingCovered.elevation = viewBeingCovered.elevation * .8f
            coveredTransitionAnimator.setTarget(viewBeingCovered)
            coveredTransitionAnimator.start()
            index++
        }
    }

    public fun removeView() {
        fragmentManager.popBackStack()
        //animateViewWhenUncovered(contentContainer.getChildAt(contentContainer.childCount - 1))
    }

    // endregion Public Functions

    // region Private Functions

    private fun createContentConatinerLayoutTransition() : LayoutTransition {
        var layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)

        layoutTransition.setDuration(LayoutTransition.APPEARING, ANIMATION_DURATION.toLong())
        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 0)

        layoutTransition.setAnimator(LayoutTransition.APPEARING, addTransitionAnimator)
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, coveredTransitionAnimator)

        return layoutTransition
    }

    private fun animateViewWhenUncovered(view: View) {
        uncoveredTransitionAnimator.setTarget(view)
        uncoveredTransitionAnimator.start()
    }

    fun addTouchListener(fragment: SwipeableFragment) {

        val startPoint = Point()

        fragment.touchListener = View.OnTouchListener { view, event ->
            try {
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {

                    startPoint.x = event.rawX.toInt()
                    startPoint.y = event.rawY.toInt()

                } else if (event.actionMasked == MotionEvent.ACTION_MOVE) {

                    view.translationX = event.rawX - startPoint.x
                    view.translationY = event.rawY - startPoint.y

                    var deltaX = view.translationX / screenWidth.toFloat()

                    view.rotation = 90 * deltaX

                } else if (event.actionMasked == MotionEvent.ACTION_UP) {
                    if ((event.rawX > leftClamp && event.rawX < rightClamp) &&
                            (event.rawY > topClamp && event.rawY < bottomClamp)) {
                        view.animate().translationX(0f).translationY(0f).rotation(0f).setDuration(300).start()
                    } else {
                        removeView()
                    }
                }
            } catch(e: Exception) {

            }

            true
        }
    }

    // endregion Private Functions
}
