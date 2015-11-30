package com.n8.intouch.main

import android.content.Context
import com.firebase.client.Firebase
import com.n8.intouch.BuildConfig
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

/**
 * Created by n8 on 11/28/15.
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, emulateSdk = 21)
class MainInteractorImplTest {

    @Mock
    lateinit var fakeContext:Context

    @Mock
    lateinit var fakeFirebase:Firebase

    @Before
    public fun setUp() {
        fakeContext = Mockito.mock(Context::class.java)
        fakeFirebase = Mockito.mock(Firebase::class.java)
    }

    @After
    public fun tearDown() {

    }

    @Test
    fun testHandleClick(){
        throw NotImplementedError()
    }

    @Test
    fun testGetData() {
        throw NotImplementedError()
    }

    private fun createInteractor(): MainInteractor{
        return MainInteractorImpl(fakeContext, fakeFirebase)
    }
}