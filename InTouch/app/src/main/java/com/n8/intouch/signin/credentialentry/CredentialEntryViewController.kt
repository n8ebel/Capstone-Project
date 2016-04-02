package com.n8.intouch.signin.credentialentry

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.n8.intouch.R
import com.n8.intouch.common.ViewUtils
import org.json.JSONObject

class CredentialEntryViewController(val userInteractionListener: CredentialEntryContract.UserInteractionListener) : CredentialEntryContract.CredentialEntryViewController,
        View.OnLayoutChangeListener {

    val STATE_KEY = "state_${CredentialEntryViewController::class.java.simpleName}"
    val STATE_KEY_USERNAME = "state_key_username"
    val STATE_KEY_PASSWORD = "state_key_passowrd"
    val STATE_KEY_ADD_ACCOUNT_BUTTON_POSITION = "state_key_add_account_button_position"

    lateinit var view:View

    lateinit var credentialEntryUsernameInputView: TextInputLayout

    lateinit var credentialEntryPasswordInputView: TextInputLayout

    lateinit var addAccountButton:View

    var addAccountButtonInInitialPosition = true

    override fun createView(inflater: LayoutInflater, parent: ViewGroup, stateBundle: Bundle?): View {
        view = inflater.inflate(R.layout.fragment_credential_entry, parent, false)

        addAccountButton = view.findViewById(R.id.floating_action_button) as FloatingActionButton
        addAccountButton.setOnClickListener(View.OnClickListener {
            animateAddAccountButton()
        })

        val signInButton = view.findViewById(R.id.credential_entry_sign_in_button) as Button
        signInButton.setOnClickListener(View.OnClickListener {
            userInteractionListener.onSignInClicked(getUsername(), getPassword())
        })

        credentialEntryUsernameInputView = view.findViewById(R.id.credential_entry_username_textInputLayout) as TextInputLayout
        credentialEntryPasswordInputView = view.findViewById(R.id.credential_entry_password_textInputLayout) as TextInputLayout
        credentialEntryUsernameInputView.hint = view.context.getString(R.string.username)
        credentialEntryPasswordInputView.hint = view.context.getString(R.string.password)

        if (stateBundle != null)  restoreState(stateBundle)

        view.addOnLayoutChangeListener(this)

        return view
    }

    override fun saveState(stateBundle: Bundle?) {
        val state = JSONObject()
        state.put(STATE_KEY_USERNAME, credentialEntryUsernameInputView.editText?.editableText.toString())
        state.put(STATE_KEY_PASSWORD, credentialEntryPasswordInputView.editText?.editableText.toString())
        state.put(STATE_KEY_ADD_ACCOUNT_BUTTON_POSITION, addAccountButtonInInitialPosition)

        stateBundle?.putString(STATE_KEY, state.toString())
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
                addAccountButtonInInitialPosition = true
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

    // region Implements OnLayoutChangeListener

    override fun onLayoutChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
        view.removeOnLayoutChangeListener(this)

        // Accounts for case where device was rotated while button was in middle of screen
        // the state bundle will indicate the inital position should be middle of screen,
        // but the views must be attached before they can be measured and the position adjusted
        //
        if (!addAccountButtonInInitialPosition) {
            var translationX = view.width / 2 - addAccountButton.width
            var translationY = view.height / 2 - addAccountButton.height

            addAccountButton.translationX = translationX.toFloat()
            addAccountButton.translationY = translationY.toFloat()
        }
    }

    override fun setUsername(username: String) {
        credentialEntryUsernameInputView.editText?.setText(username)
    }

    // endregion Implements OnLayoutChangeListner

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
                addAccountButtonInInitialPosition = false
                userInteractionListener.onAddNewAccountClicked()
            }
        }))

        animSet.start()
    }

    private fun restoreState(state: Bundle) {
        val stateString = state.getString(STATE_KEY) ?: return

        val stateJson = JSONObject(stateString)

        val username = stateJson.optString(STATE_KEY_USERNAME)
        val password = stateJson.optString(STATE_KEY_PASSWORD)
        addAccountButtonInInitialPosition = stateJson.getBoolean(STATE_KEY_ADD_ACCOUNT_BUTTON_POSITION)

        if (username != null) {
            credentialEntryUsernameInputView.editText?.setText(username)
        }

        if (password != null) {
            credentialEntryPasswordInputView.editText?.setText(password)
        }

    }

    private fun getUsername() : String {
        return credentialEntryUsernameInputView.editText?.text.toString()
    }

    private fun getPassword() : String {
        return credentialEntryPasswordInputView.editText?.text.toString()
    }
}