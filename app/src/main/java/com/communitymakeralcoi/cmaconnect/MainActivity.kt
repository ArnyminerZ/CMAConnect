package com.communitymakeralcoi.cmaconnect

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.communitymakeralcoi.cmaconnect.api.auth.user
import com.communitymakeralcoi.cmaconnect.api.config.google_play_available
import com.communitymakeralcoi.cmaconnect.api.config.sharedPreferences
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.dialog.Dialog
import com.communitymakeralcoi.cmaconnect.api.fragment.BottomNavigationDrawerFragment
import com.communitymakeralcoi.cmaconnect.api.fragment.RC_SIGN_IN
import com.communitymakeralcoi.cmaconnect.api.utils.DialogButton
import com.communitymakeralcoi.cmaconnect.api.utils.IntentUtils.openPlayStore
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds


class MainActivity : CMAActivity() {
    val mAuth = FirebaseAuth.getInstance()

    override fun setupView() {
        when (ui_version) {
            1 -> {
                setTheme(R.style.Style1_NoTitle)
            }
        }
    }

    override fun updateView() {
        when (ui_version) {
            1 -> {
                if (firstLaunch) {
                    setContentView(R.layout.activity_main)

                    setSupportActionBar(bottom_app_bar)

                    //<editor-fold desc="Ads loading">
                    if (BuildConfig.DEBUG)
                        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111") // Testing key
                    else
                        MobileAds.initialize(this, "ca-app-pub-1835953360087762~7251664013") // Own key
                    val adRequest = AdRequest.Builder().build()
                    try{
                        main_adView.loadAd(adRequest)
                    }catch (ex: IllegalStateException){
                        Log.e(tag, "Could not load MainActivity AdBanner in cause of IllegalStateException")
                        main_adView.visibility = View.GONE
                    }
                    //</editor-fold>

                    fab.setOnClickListener {
                        // TODO: Transition to Devices Activity
                        startActivity(Intent(this@MainActivity, DevicesActivity().javaClass))
                    }

                    adsMessageDismiss_button.setOnClickListener {
                        with(sharedPreferences.edit()){
                            putBoolean("showAdsMessage", false)
                            apply()
                        }
                        val exitAnimation = AnimationUtils.loadAnimation(this, R.anim.exit_to_right)
                        exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationRepeat(animation: Animation?) { }
                            override fun onAnimationStart(animation: Animation?) { }

                            override fun onAnimationEnd(animation: Animation?) {
                                updateView()
                            }
                        })
                        adsMessage.startAnimation(exitAnimation)
                    }
                }

                user = mAuth.currentUser

                if(!sharedPreferences.getBoolean("showAdsMessage", true)) adsMessage.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra("redirectLoad", true))
            startActivity(Intent(this, LoadActivity().javaClass))
        //<editor-fold desc="Update dialog">
        if (intent.getBooleanExtra("runningOldVersion", false)) {
            Dialog(this, R.string.new_version_available_dialog_title, R.string.new_version_available_dialog_message,
                    DialogButton(R.string.action_download) { dialog: DialogInterface, _: Int ->
                        if (google_play_available) openPlayStore(this) else {
                            // TODO: Download apk
                        }
                        dialog.dismiss()
                    }, DialogButton(R.string.action_cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }, null).show()
        }
        //</editor-fold>
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                updateView()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.v(tag, "Options item selected")
        val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
        bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)

        return true
    }
}
