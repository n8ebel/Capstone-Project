package com.n8.intouch.signin.di

import com.n8.intouch.signin.addaccount.AddAccountContract
import com.n8.intouch.signin.addaccount.AddAccountViewController
import com.n8.intouch.signin.credentialentry.CredentialEntryContract
import com.n8.intouch.signin.credentialentry.CredentialEntryViewController
import dagger.Module
import dagger.Provides

@Module
class SignInModule(
    val credentialEntryInteractionListener:CredentialEntryContract.UserInteractionListener,
    val addAccountInteractionListener:AddAccountContract.UserInteractionListener) {

    @Provides
    fun providesCredentialEntryViewController() : CredentialEntryContract.CredentialEntryViewController {
        return CredentialEntryViewController(credentialEntryInteractionListener)
    }

    @Provides
    fun providesAddAccountViewController() : AddAccountContract.AddAccountViewController {
        return AddAccountViewController(addAccountInteractionListener)
    }
}