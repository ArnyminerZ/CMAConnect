package com.communitymakeralcoi.cmaconnect.api.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.communitymakeralcoi.cmaconnect.ProfileActivity
import com.communitymakeralcoi.cmaconnect.R
import com.communitymakeralcoi.cmaconnect.SettingsActivity
import com.communitymakeralcoi.cmaconnect.api.auth.user
import com.communitymakeralcoi.cmaconnect.api.utils.NetworkUtils
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import java.util.*


val RC_SIGN_IN = 123

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        val sessionContainer = view.findViewById<LinearLayout>(R.id.session_container)
        val sessionName = view.findViewById<TextView>(R.id.session_name)

        if (user == null) {
            // User not logged in
            sessionContainer.setOnClickListener {
                // TODO: Custom Login
                //if (!loggedIn) startActivity(Intent(activity, LoginActivity().javaClass))

                // Choose authentication providers
                val providers = Arrays.asList(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build())

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                //.setPrivacyPolicyUrl("https://superapp.example.com/privacy-policy.html")
                                // TODO: Set privacy policy url
                                .build(),
                        RC_SIGN_IN)
            }
        } else {
            val userName = user!!.displayName
            val userEmail = user!!.email
            val userPhone = user!!.phoneNumber

            if (userName == null || userName == "")
                sessionName.text = user!!.email
            else if (userEmail == null || userEmail == "")
                sessionName.text = userEmail
            else if (userPhone == null || userPhone == "")
                sessionName.text = userPhone
            else
                sessionName.text = getString(R.string.no_valid_name)
            sessionContainer.setOnClickListener {
                startActivity(Intent(context, ProfileActivity().javaClass))
                dismiss()
            }
        }

        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                R.id.settings_nav_button -> {
                    dismiss()
                    startActivity(Intent(context, SettingsActivity().javaClass))
                }
            }
            true
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (user != null)
            for (userInfo in user!!.providerData) {
                when (userInfo.providerId) {
                    "google.com" -> {
                        val userProfileImageUrl = user!!.photoUrl
                        if (userProfileImageUrl != null) NetworkUtils.setImageViewFromUrl(session_image, userProfileImageUrl, 8000)
                    }
                }
            }
    }
}