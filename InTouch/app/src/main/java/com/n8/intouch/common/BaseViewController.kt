package com.n8.intouch.common

abstract class BaseViewController : ViewController {

    override fun showView() {
        showView {  }
    }

    override fun showView(function: () -> Unit) {
        // noop
    }

    override fun hideView() {
        hideView {  }
    }

    override fun hideView(function: () -> Unit) {
        // noop
    }
}