package com.n8.intouch.main

/**
 * Created by n8 on 11/21/15.
 */
class MainPresenterImpl(val mainView : MainView, val mainInteractor : MainInteractor) : MainPresenter {
    override fun onClick() {
        mainView.showProgress()

        mainInteractor.handleClick { result ->
            mainView.hideProgress()

            mainView.showResult(result)
        }
    }
}