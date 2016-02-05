package com.n8.intouch.signin.credentialentry

import com.n8.intouch.common.ViewController

class CredentialEntryContract {

    public interface CredentialEntryViewController : ViewController {

    }

    public interface UserInteractionListener {
        fun onUsernameUpdated(username:String)

        fun onPasswordUpdated(password:String)

        fun onAddNewAccountClicked()

        fun onSignInClicked()
    }
}