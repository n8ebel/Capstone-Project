package com.n8.intouch.main

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

    override fun showData(arg1: String, arg2: Array<String>) {
        mainView.showProgress()

        try {
            mainInteractor.getData(arg1, arg2)
        }catch(e:Exception){
            handleError(Exception("Exception correctly thrown when accessing content provider"))
        }finally{
            mainView.hideProgress()
        }
    }

    override fun handleError(exception: Exception) {
        mainView.showResult(exception.message ?: "error")
    }

    override fun scheduleText(phoneNumber: String) {
        mainInteractor.scheduleText(phoneNumber)
    }
}