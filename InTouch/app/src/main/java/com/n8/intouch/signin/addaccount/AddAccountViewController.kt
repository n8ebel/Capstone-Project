package com.n8.intouch.signin.addaccount

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.ViewUtils

class AddAccountViewController(var userInteractionListener: AddAccountContract.UserInteractionListener) : AddAccountContract.AddAccountViewController {

    lateinit var view:View

    lateinit var addAccountUsernameInputView: TextInputLayout

    lateinit var addAccountPasswordInputView: TextInputLayout

    lateinit var submitButton:View

    override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
        view = inflater.inflate(R.layout.fragment_add_account, parent, false)

        addAccountUsernameInputView = view.findViewById(R.id.add_account_username_textInputLayout) as TextInputLayout
        addAccountPasswordInputView = view.findViewById(R.id.add_account_password_textInputLayout) as TextInputLayout
        addAccountUsernameInputView.hint = "Username"
        addAccountPasswordInputView.hint = "Password"

        submitButton = view.findViewById(R.id.add_account_submit_button)
        submitButton.setOnClickListener(View.OnClickListener {
            userInteractionListener.onAddAccountClicked()
        })

        return view
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
                run(function)
            }
        })
    }

    // endregion Implements Contract.ViewController
}