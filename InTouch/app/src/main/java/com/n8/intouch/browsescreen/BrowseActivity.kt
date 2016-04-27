package com.n8.intouch.browsescreen

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.n8.intouch.R
import com.n8.intouch.settings.SettingsActivity
import com.n8.intouch.browsescreen.di.BrowseModule
import com.n8.intouch.browsescreen.di.DaggerBrowseComponent
import com.n8.intouch.common.BaseActivity
import com.n8.intouch.common.CircularTransform
import com.n8.intouch.getComponent
import com.squareup.picasso.Picasso

class BrowseActivity : BaseActivity() {

    companion object {
        val TAG_BROWSE_FRAGMENT = "BrowseActivity"
    }

    lateinit var navigationView:NavigationView

    lateinit var usernameTextView:TextView

    lateinit var mToolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mToolbar = (findViewById(R.id.toolbar) as Toolbar).apply {
            // Adding the extra space to fix the text being cut off
            title = getString(R.string.app_name) + " "
        }

        val drawer = findViewById(R.id.browse_drawerLayout) as DrawerLayout

        val drawerToggle = ActionBarDrawerToggle(this, drawer, mToolbar, R.string.open_drawer, R.string.close_drawer)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView = findViewById(R.id.browse_navigationView) as NavigationView
        val navigationViewHeader = layoutInflater.inflate(R.layout.navigation_view_header, navigationView, false)
        navigationView.addHeaderView(navigationViewHeader)
        navigationView.inflateMenu(R.menu.navigation_view_menu)
        navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.drawer_sign_out -> confirmSignOut()
                R.id.drawer_settings -> showSettings()
            }
            drawer.closeDrawers()
            true
        }

        usernameTextView = navigationViewHeader.findViewById(R.id.textView) as TextView
        val profileImage = navigationViewHeader.findViewById(R.id.imageView) as ImageView

        val currentUser = application.getComponent().getCurrentUser()

        usernameTextView.text = currentUser.getUsername()
        Picasso.with(this).load(currentUser.getProfileImageUrl()).transform(CircularTransform()).into(profileImage)

        (findViewById(R.id.adView) as AdView).apply {
            loadAd(AdRequest.Builder().build())
        }

        // Only add the tabbed fragment the first time the activity is created
        //
        if (savedInstanceState == null) {
            var browseFragment = BrowseFragment().apply {
                component = DaggerBrowseComponent.builder().
                        applicationComponent(application.getComponent()).
                        browseModule(BrowseModule(this)).
                        build()
            }

            with(supportFragmentManager.beginTransaction()){
                add(R.id.fragmentContainer, browseFragment, TAG_BROWSE_FRAGMENT)
                addToBackStack(TAG_BROWSE_FRAGMENT)
                commit()
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        supportFragmentManager.findFragmentByTag(TAG_BROWSE_FRAGMENT)?.onActivityResult(requestCode, resultCode, data)
    }

    private fun confirmSignOut() {
        AlertDialog.Builder(this)
            .setTitle(R.string.sign_out)
            .setMessage(R.string.sign_out_confirmation)
            .setPositiveButton(android.R.string.ok, { p0, p1 ->
                application.getComponent().getFirebase().unauth()
                finish()
            })
            .setNeutralButton(android.R.string.cancel, { p0, p1 ->
                
            })
            .show()
    }

    private fun showSettings() {
        startActivity(SettingsActivity.createIntent(this))
    }
}
