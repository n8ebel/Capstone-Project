package com.n8.intouch.main

import android.content.ContentResolver
import com.n8.intouch.contentprovider.ProviderContract

/**
 * Created by n8 on 11/21/15.
 */
class MainPresenterImpl(val mainView : MainView, val mainInteractor : MainInteractor) : MainPresenter {
    override fun showFirebaseToast() {
        mainView.showProgress()

        mainInteractor.handleClick { result ->
            mainView.hideProgress()

            mainView.showResult(result)
        }
    }

    override fun showData(contentResolver: ContentResolver, arg1: String, arg2: Array<String>) {
        mainView.showProgress()

        try {
            mainInteractor.getData(contentResolver, arg1, arg2)
        }catch(e:Exception){
            handleError(Exception("Exception correctly thrown when accessing content provider"))
        }finally{
            mainView.hideProgress()
        }
    }

    override fun handleError(exception: Exception) {
        mainView.showResult(exception.message ?: "error")
    }
}