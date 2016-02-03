package com.n8.intouch.signin.credentialentry

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.ViewUtils

class CredentialEntryViewController(val userInteractionListener: CredentialEntryContract.UserInteractionListener) : CredentialEntryContract.CredentialEntryViewController {

    lateinit var view:View

    lateinit var credentialEntryUsernameInputView: TextInputLayout

    lateinit var credentialEntryPasswordInputView: TextInputLayout

    lateinit var addAccountButton:View

    override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
        view = inflater.inflate(R.layout.fragment_credential_entry, parent, false)

        addAccountButton = view.findViewById(R.id.floating_action_button) as FloatingActionButton
        addAccountButton.setOnClickListener(View.OnClickListener {
            animateAddAccountButton()
        })

        credentialEntryUsernameInputView = view.findViewById(R.id.credential_entry_username_textInputLayout) as TextInputLayout
        credentialEntryPasswordInputView = view.findViewById(R.id.credential_entry_password_textInputLayout) as TextInputLayout
        credentialEntryUsernameInputView.hint = "Username"
        credentialEntryPasswordInputView.hint = "Password"

        return view
    }

    override fun showView() {
        showView {  }
    }

    override fun showView(function: () -> Unit) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            ViewUtils.revealView(view)
        }

        val xAnim = ObjectAnimator.ofFloat(addAccountButton, "translationX", 0f)
        xAnim.interpolator = FastOutLinearInInterpolator()
        xAnim.setDuration(500)

        val yAnim = ObjectAnimator.ofFloat(addAccountButton, "translationY", 0f)
        yAnim.interpolator = LinearOutSlowInInterpolator()
        yAnim.setDuration(500)

        val animSet = AnimatorSet()
        animSet.playTogether(xAnim, yAnim)

        animSet.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                run(function)
            }
        })

        animSet.start()
    }

    override fun hideView() {
        hideView {  }
    }

    override fun hideView(function: () -> Unit) {
        view.visibility = View.INVISIBLE
        run(function)
    }

    // endregion Implements Contract.ViewController

    private fun animateAddAccountButton(){
        var translationX = view.width / 2 - addAccountButton.width
        var translationY = view.height / 2 - addAccountButton.height

        val xAnim = ObjectAnimator.ofFloat(addAccountButton, "translationX", -1*translationX.toFloat())
        xAnim.interpolator = FastOutLinearInInterpolator()
        xAnim.setDuration(500)

        val yAnim = ObjectAnimator.ofFloat(addAccountButton, "translationY", translationY.toFloat())
        yAnim.interpolator = LinearOutSlowInInterpolator()
        yAnim.setDuration(500)

        val animSet = AnimatorSet()
        animSet.playTogether(xAnim, yAnim)
        animSet.addListener((object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                userInteractionListener.onAddAccountClicked()
            }
        }))

        animSet.start()
    }
}