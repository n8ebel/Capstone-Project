package com.n8.intouch.model

import com.firebase.client.AuthData

/**
 * Represents a user currenty logged in to the specified Firebase.AuthData reference
 */
class FirebaseUser(private val authData: AuthData) : User {
    override fun getId(): String {
        return authData.uid
    }

    override fun getUsername(): String {
        return authData.providerData["email"] as String
    }

    override fun getProfileImageUrl(): String {
        return authData.providerData["profileImageURL"] as String
    }
}