package com.n8.intouch.model

/**
 * Interface to represent a unique, logged in user
 */
interface User {
    fun getId() : String
    fun getUsername() : String
}