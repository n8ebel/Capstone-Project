package com.n8.intouch.signin.addaccount

import com.n8.intouch.common.ViewController

class AddAccountContract {

    interface AddAccountViewController : ViewController {

    }

    interface UserInteractionListener {
        fun onCreateAccountClicked(username:String, password:String)
    }
}