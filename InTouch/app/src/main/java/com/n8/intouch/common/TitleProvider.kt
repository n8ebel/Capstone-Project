package com.n8.intouch.common

/**
 * Defines that an object can provider a title for itself
 */
interface TitleProvider {
    fun getTitle():String
}