package com.n8.intouch.signin

import android.animation.*
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.BaseActivity

class SignInActivity : BaseActivity(), View.OnLayoutChangeListener {

    lateinit var rootContentView:ViewGroup

    lateinit var credentialEntryView:View

    lateinit var addAccountView:View

    lateinit var fab:FloatingActionButton

    lateinit var credentialEntryUsernameInputView: TextInputLayout

    lateinit var credentialEntryPasswordInputView: TextInputLayout

    lateinit var addAccountUsernameInputView: TextInputLayout

    lateinit var addAccountPasswordInputView: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        rootContentView = findViewById(R.id.content_container) as ViewGroup

        addAccountView = layoutInflater!!.inflate(R.layout.fragment_add_account, rootContentView, false)
        credentialEntryView = layoutInflater.inflate(R.layout.fragment_credential_entry, rootContentView, false)

        addAccountView.visibility = View.INVISIBLE
        credentialEntryView.visibility = View.INVISIBLE

        rootContentView.addView(credentialEntryView)
        rootContentView.addView(addAccountView)

        rootContentView.addOnLayoutChangeListener(this)

        fab = credentialEntryView.findViewById(R.id.floating_action_button) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener {
            showAddAccountView()
        })

        // Credential Entry View
        //
        credentialEntryUsernameInputView = rootContentView.findViewById(R.id.credential_entry_username_textInputLayout) as TextInputLayout
        credentialEntryPasswordInputView = rootContentView.findViewById(R.id.credential_entry_password_textInputLayout) as TextInputLayout
        credentialEntryUsernameInputView.hint = "Username"
        credentialEntryPasswordInputView.hint = "Password"

        // Add Account View
        addAccountUsernameInputView = rootContentView.findViewById(R.id.add_account_username_textInputLayout) as TextInputLayout
        addAccountPasswordInputView = rootContentView.findViewById(R.id.add_account_password_textInputLayout) as TextInputLayout
        addAccountUsernameInputView.hint = "Username"
        addAccountPasswordInputView.hint = "Password"
    }

    override fun onBackPressed() {
        if (addAccountView.visibility == View.VISIBLE) {
            hideAddAccountView()
            return
        }

        super.onBackPressed()
    }

    // region Implements OnLayoutChangeListener

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        rootContentView.removeOnLayoutChangeListener(this)

        revealView(credentialEntryView, rootContentView)
    }

    // region Implements OnLayoutChangeListener

    private fun revealView(view:View, parent:View){
        // get the center for the clipping circle
        var cx = parent.width / 2;
        var cy = parent.height / 2;

        // get the final radius for the clipping circle
        var finalRadius = Math.hypot(cx.toDouble(), cy.toDouble());

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat()).setDuration(700)

        // make the view visible and start the animation
        view.visibility = View.VISIBLE

        anim.start()
    }

    private fun getFinalRevealRadius(view:View) : Double {
        var cx = view.width / 2;
        var cy = view.height / 2;

        // get the final radius for the clipping circle
        return Math.hypot(cx.toDouble(), cy.toDouble());
    }

    private fun showAddAccountView() {
        var translationX = rootContentView.width / 2 - fab.width
        var translationY = rootContentView.height / 2 - fab.height

        val xAnim = ObjectAnimator.ofFloat(fab, "translationX", -1*translationX.toFloat())
        xAnim.interpolator = FastOutLinearInInterpolator()
        xAnim.setDuration(500)

        val yAnim = ObjectAnimator.ofFloat(fab, "translationY", translationY.toFloat())
        yAnim.interpolator = LinearOutSlowInInterpolator()
        yAnim.setDuration(500)

        val animSet = AnimatorSet()
        animSet.playTogether(xAnim, yAnim)
        animSet.addListener((object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                ViewAnimationUtils.createCircularReveal(
                        addAccountView,
                        (addAccountView.width / 2).toInt(),
                        (addAccountView.height / 2).toInt(),
                        0f,
                        getFinalRevealRadius(addAccountView).toFloat()
                ).setDuration(1000).start()
                addAccountView.visibility = View.VISIBLE
            }
        }))

        animSet.start()
    }

    private fun hideAddAccountView(){
        val reveal = ViewAnimationUtils.createCircularReveal(
                addAccountView,
                (addAccountView.width / 2).toInt(),
                (addAccountView.height / 2).toInt(),
                getFinalRevealRadius(addAccountView).toFloat(),
                0f
        ).setDuration(1000)

        reveal.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                addAccountView.visibility = View.INVISIBLE

                val xAnim = ObjectAnimator.ofFloat(fab, "translationX", 0f)
                xAnim.interpolator = FastOutLinearInInterpolator()
                xAnim.setDuration(500)

                val yAnim = ObjectAnimator.ofFloat(fab, "translationY", 0f)
                yAnim.interpolator = LinearOutSlowInInterpolator()
                yAnim.setDuration(500)

                val animSet = AnimatorSet()
                animSet.playTogether(xAnim, yAnim)

                animSet.start()
            }
        })

        reveal.start()
    }
}