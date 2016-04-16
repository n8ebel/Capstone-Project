package com.n8.intouch.messageentryscreen

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.Mockito.*

class MessageEntryPresenterTest {

    companion object {
        val PHONE_NUMBER = "1111111"
        val EMPTY_PHONE_NUMBER = ""
        val MESSAGE = "message"
        val EMPTY_MESSAGE = ""
    }

    lateinit var mMockViewController:Contract.ViewListener
    lateinit var mMockFragmentListener:MessageEntryFragment.Listener

    @Before
    fun setup() {
        mMockViewController = Mockito.mock(Contract.ViewListener::class.java)
        mMockFragmentListener = Mockito.mock(MessageEntryFragment.Listener::class.java)
    }

    @Test
    fun testStart() {
        createPresenter().apply {
            start()
        }

        Mockito.verify(mMockViewController).setPhoneNumber(PHONE_NUMBER)

    }

    @Test
    fun testStop() {

    }

    @Test
    fun testOnMessageTextChanged_NoMessage_NoPhoneNumber() {
        createPresenter(EMPTY_PHONE_NUMBER).apply {
            onMessageTextChanged(EMPTY_MESSAGE)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    @Test
    fun testOnMessageTextChanged_NoPhoneNumber() {
        createPresenter(EMPTY_PHONE_NUMBER).apply {
            onMessageTextChanged(MESSAGE)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    fun testOnMessageTextChanged_NoMessage_WithPhoneNumber() {

        createPresenter().apply {
            onMessageTextChanged(EMPTY_MESSAGE)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    fun testOnMessageTextChanged_WithPhoneNumber() {
        createPresenter().apply {
            onMessageTextChanged(MESSAGE)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(true)
    }

    @Test
    fun testOnPhoneNumberTextChanged_NoNumber_NoMessage() {
        createPresenter().apply {
            onPhoneNumberTextChanged(EMPTY_PHONE_NUMBER)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    @Test
    fun testOnPhoneNumberTextChanged_NoMessage() {
        createPresenter().apply {
            onPhoneNumberTextChanged(PHONE_NUMBER)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    fun testOnPhoneNumberTextChanged_NoNumber_WithMessage() {
        createPresenter().apply {
            onMessageTextChanged(MESSAGE)
            onPhoneNumberTextChanged(EMPTY_PHONE_NUMBER)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
    }

    fun testOnPhoneNumberTextChanged_WithMessage() {
        createPresenter().apply {
            onMessageTextChanged(MESSAGE)
            onPhoneNumberTextChanged(PHONE_NUMBER)
        }

        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(false)
        Mockito.verify(mMockViewController, times(1)).setContinueButtonVisible(true)
    }

    @Test
    fun testOnContinueClicked() {
        createPresenter().apply {
            onMessageTextChanged(MESSAGE)
            onContinueClicked()
        }

        verify(mMockFragmentListener, times(1)).onMessageEntered(PHONE_NUMBER, MESSAGE)
    }

    // region Private Methods

    private fun createPresenter(phoneNumber:String = PHONE_NUMBER) : MessageEntryPresenter {
        return MessageEntryPresenter(phoneNumber, mMockViewController, mMockFragmentListener)
    }

    // endregion Private Methods
}