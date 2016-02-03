package com.n8.intouch.signin

import android.animation.*
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.BaseActivity
import com.n8.intouch.common.ViewController
import com.n8.intouch.common.ViewUtils
import com.n8.intouch.signin.addaccount.AddAccountContract
import com.n8.intouch.signin.credentialentry.CredentialEntryContract
import com.n8.intouch.signin.di.DaggerSignInComponent
import com.n8.intouch.signin.di.SignInModule
import javax.inject.Inject

class SignInActivity : BaseActivity(), View.OnLayoutChangeListener {

    @Inject
    lateinit var credentialEntryViewController : CredentialEntryContract.CredentialEntryViewController

    @Inject
    lateinit var addAccountViewController : AddAccountContract.AddAccountViewController

    lateinit var currentViewController : ViewController

    lateinit var rootContentView:ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSignInComponent.builder()
                .signInModule(SignInModule(
                        createCredentialEntryInteractionListener(),
                        createAddAccountInteractionListener())
                )
                .build()
                .inject(this)

        setContentView(R.layout.activity_sign_in)
        rootContentView = findViewById(R.id.content_container) as ViewGroup

        currentViewController = credentialEntryViewController

        val credentialEntryView = credentialEntryViewController.createView(layoutInflater, rootContentView)
        val addAccountView = addAccountViewController.createView(layoutInflater, rootContentView)

        rootContentView.addView(credentialEntryView)
        rootContentView.addView(addAccountView)

        addAccountView.visibility = View.INVISIBLE
        credentialEntryView.visibility = View.INVISIBLE

        rootContentView.addOnLayoutChangeListener(this)
    }

    override fun onBackPressed() {
        if (currentViewController == addAccountViewController) {
            addAccountViewController.hideView({
                currentViewController = credentialEntryViewController
                credentialEntryViewController.showView { }
            })
            return
        }

        super.onBackPressed()
    }

    // region Implements OnLayoutChangeListener

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        rootContentView.removeOnLayoutChangeListener(this)
        credentialEntryViewController.showView {  }
    }

    // region Implements OnLayoutChangeListener

    // region Private Functions

    private fun createAddAccountInteractionListener() : AddAccountContract.UserInteractionListener {
        return object:AddAccountContract.UserInteractionListener{
            override fun onUsernameUpdated(username: String) {
                throw UnsupportedOperationException()
            }

            override fun onAddAccountClicked() {
                throw UnsupportedOperationException()
            }

            override fun onPasswordUpdated(password: String) {
                throw UnsupportedOperationException()
            }

        }
    }

    private fun createCredentialEntryInteractionListener() : CredentialEntryContract.UserInteractionListener {
        return object:CredentialEntryContract.UserInteractionListener{
            override fun onUsernameUpdated(username: String) {
                throw UnsupportedOperationException()
            }

            override fun onPasswordUpdated(password: String) {
                throw UnsupportedOperationException()
            }

            override fun onAddAccountClicked() {
                currentViewController = addAccountViewController
                addAccountViewController.showView({})
            }

            override fun onSignInClicked() {
                throw UnsupportedOperationException()
            }

        }
    }

    // endregion Private Functions
}