package com.n8.intouch.signin.addaccount

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.ViewUtils
import com.n8.intouch.signin.credentialentry.CredentialEntryViewController
import org.json.JSONObject

class AddAccountViewController(var userInteractionListener: AddAccountContract.UserInteractionListener) : AddAccountContract.AddAccountViewController {

    val STATE_KEY = "state_${AddAccountViewController::class.java.simpleName}"
    val STATE_KEY_USERNAME = "state_key_username"
    val STATE_KEY_PASSWORD = "state_key_passowrd"

    lateinit var view:View

    lateinit var addAccountUsernameInputView: TextInputLayout

    lateinit var addAccountPasswordInputView: TextInputLayout

    lateinit var submitButton:View

    override fun createView(inflater: LayoutInflater, parent: ViewGroup, stateBundle: Bundle?): View {
        view = inflater.inflate(R.layout.fragment_add_account, parent, false)

        addAccountUsernameInputView = view.findViewById(R.id.add_account_username_textInputLayout) as TextInputLayout
        addAccountPasswordInputView = view.findViewById(R.id.add_account_password_textInputLayout) as TextInputLayout
        addAccountUsernameInputView.hint = view.context.getString(R.string.username)
        addAccountPasswordInputView.hint = view.context.getString(R.string.password)

        submitButton = view.findViewById(R.id.add_account_submit_button)
        submitButton.setOnClickListener(View.OnClickListener {
            userInteractionListener.onCreateAccountClicked(getUsername(), getPassword())
        })

        if (stateBundle != null)  restoreState(stateBundle)

        return view
    }

    override fun saveState(stateBundle: Bundle?) {
        val state = JSONObject()
        state.put(STATE_KEY_USERNAME, addAccountUsernameInputView.editText?.editableText.toString())
        state.put(STATE_KEY_PASSWORD, addAccountPasswordInputView.editText?.editableText.toString())

        stateBundle?.putString(STATE_KEY, state.toString())
    }

    override fun showView() {
        showView {  }
    }

    override fun showView(function: () -> Unit) {
        ViewUtils.revealView(view, object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                run(function)
            }
        })
    }

    override fun hideView() {
        hideView {  }
    }

    override fun hideView(function: () -> Unit) {
        ViewUtils.hideView(view, object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                addAccountUsernameInputView.editText?.setText("")
                addAccountPasswordInputView.editText?.setText("")
                run(function)
            }
        })
    }

    // endregion Implements Contract.ViewController

    private fun restoreState(state: Bundle) {
        val stateString = state.getString(STATE_KEY) ?: return

        val stateJson = JSONObject(stateString)

        val username = stateJson.optString(STATE_KEY_USERNAME)
        val password = stateJson.optString(STATE_KEY_PASSWORD)

        if (username != null) {
            addAccountUsernameInputView.editText?.setText(username)
        }

        if (password != null) {
            addAccountPasswordInputView.editText?.setText(password)
        }

    }

    private fun getUsername() : String {
        return addAccountUsernameInputView.editText?.text.toString()
    }

    private fun getPassword() : String {
        return addAccountPasswordInputView.editText?.text.toString()
    }
}