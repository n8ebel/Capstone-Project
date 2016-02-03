package com.n8.intouch.signin.addaccount

import com.n8.intouch.common.ViewController

class AddAccountContract {

    public interface AddAccountViewController : ViewController {

    }

    public interface UserInteractionListener {
        fun onUsernameUpdated(username:String)

        fun onPasswordUpdated(password:String)

        fun onAddAccountClicked()
    }
}