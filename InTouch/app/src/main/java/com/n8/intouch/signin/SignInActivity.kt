package com.n8.intouch.signin

import android.animation.*
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.BaseActivity
import com.n8.intouch.common.ViewController
import com.n8.intouch.common.ViewUtils
import com.n8.intouch.signin.addaccount.AddAccountContract
import com.n8.intouch.signin.credentialentry.CredentialEntryContract
import com.n8.intouch.signin.credentialentry.CredentialEntryViewController
import com.n8.intouch.signin.di.DaggerSignInComponent
import com.n8.intouch.signin.di.SignInModule
import org.json.JSONObject
import javax.inject.Inject

class SignInActivity : BaseActivity(), View.OnLayoutChangeListener {

    val STATE_KEY = "state_${SignInActivity::class.java.simpleName}"
    val STATE_KEY_ADD_ACCOUNT_VISIBLE = "state_key_add_account_visible"

    @Inject
    lateinit var credentialEntryViewController : CredentialEntryContract.CredentialEntryViewController

    @Inject
    lateinit var addAccountViewController : AddAccountContract.AddAccountViewController

    lateinit var currentViewController : ViewController

    lateinit var credentialEntryView : View

    lateinit var addAccountView : View

    lateinit var rootContentView:ViewGroup

    var addAccountVisible = false

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

        credentialEntryView = credentialEntryViewController.createView(layoutInflater, rootContentView, savedInstanceState)
        addAccountView = addAccountViewController.createView(layoutInflater, rootContentView, savedInstanceState)

        rootContentView.addView(credentialEntryView)
        rootContentView.addView(addAccountView)

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        credentialEntryView.visibility = if(savedInstanceState != null) View.VISIBLE else View.INVISIBLE
        addAccountView.visibility = if (addAccountVisible) View.VISIBLE else View.INVISIBLE

        rootContentView.addOnLayoutChangeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        credentialEntryViewController.saveState(outState)
        addAccountViewController.saveState(outState)

        val state = JSONObject()

        state.put(STATE_KEY_ADD_ACCOUNT_VISIBLE, addAccountVisible)

        outState?.putString(STATE_KEY, state.toString())
    }

    override fun onBackPressed() {
        if (currentViewController == addAccountViewController) {
            addAccountViewController.hideView({
                addAccountVisible = false
                currentViewController = credentialEntryViewController
                credentialEntryViewController.showView()
            })
            return
        }

        super.onBackPressed()
    }

    // region Implements OnLayoutChangeListener

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        rootContentView.removeOnLayoutChangeListener(this)
        if (credentialEntryView.visibility != View.VISIBLE) {
            credentialEntryViewController.showView()
        }
    }

    // region Implements OnLayoutChangeListener

    // region Private Functions

    private fun createAddAccountInteractionListener() : AddAccountContract.UserInteractionListener {
        return object:AddAccountContract.UserInteractionListener{
            override fun onUsernameUpdated(username: String) {
                throw UnsupportedOperationException()
            }

            override fun onCreateAccountClicked() {
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

            override fun onAddNewAccountClicked() {
                currentViewController = addAccountViewController
                addAccountViewController.showView()
                addAccountVisible = true
            }

            override fun onSignInClicked() {
                throw UnsupportedOperationException()
            }

        }
    }

    private fun restoreState(state: Bundle) {
        val stateString = state.getString(STATE_KEY) ?: return

        val stateJson = JSONObject(stateString)

        addAccountVisible = stateJson.getBoolean(STATE_KEY_ADD_ACCOUNT_VISIBLE)
        if (addAccountVisible) {
            currentViewController = addAccountViewController
        }
    }

    // endregion Private Functions
}