package com.n8.intouch.signin

import android.app.Application
import android.os.Parcel
import android.support.test.runner.AndroidJUnit4
import android.test.ApplicationTestCase
import android.test.suitebuilder.annotation.SmallTest
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@SmallTest
class SignInAsyncTaskTest {

    companion object {
        val USERNAME = "username"
        val PASSWORD = "password"

        val AUTH_HANDLER = object : Firebase.AuthResultHandler {
            override fun onAuthenticationError(p0: FirebaseError?) {
                throw UnsupportedOperationException()
            }

            override fun onAuthenticated(p0: AuthData?) {
                throw UnsupportedOperationException()
            }

        }
    }

    @Test
    fun testDoInBackground() {
        var firebase = Mockito.mock(Firebase::class.java)

        var latch = CountDownLatch(1)
        var task = object : SignInAsyncTask(firebase, SignInAsyncTaskTest.AUTH_HANDLER){
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                latch.countDown()
            }
        }
        task.execute(USERNAME, PASSWORD)

        latch.await()

        verify(firebase).authWithPassword(USERNAME, PASSWORD, AUTH_HANDLER)
    }

}