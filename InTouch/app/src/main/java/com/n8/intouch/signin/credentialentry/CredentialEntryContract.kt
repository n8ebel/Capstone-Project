package com.n8.intouch.signin.credentialentry

import com.n8.intouch.common.ViewController

class CredentialEntryContract {

    interface CredentialEntryViewController : ViewController {

    }

    interface UserInteractionListener {
        fun onAddNewAccountClicked()

        fun onSignInClicked(username:String, password:String)
    }
}