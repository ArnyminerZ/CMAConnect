package com.communitymakeralcoi.cmaconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.communitymakeralcoi.cmaconnect.api.auth.changeEmail
import com.communitymakeralcoi.cmaconnect.api.auth.changeUsername
import com.communitymakeralcoi.cmaconnect.api.auth.uploadProfileImage
import com.communitymakeralcoi.cmaconnect.api.auth.user
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.utils.NetworkUtils
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.*


class ProfileActivity : CMAActivity() {
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
                    setContentView(R.layout.activity_profile)

                    (findViewById<ImageButton>(R.id.profile_imageButton)).setOnClickListener {
                        ImagePicker.create(this)
                                .single()
                                .includeVideo(false)
                                .folderMode(true)
                                .toolbarFolderTitle(getString(R.string.picker_folder_title))
                                .toolbarImageTitle(getString(R.string.picker_image_title))
                                .start()
                    }

                    (findViewById<Button>(R.id.nameSave_button)).setOnClickListener {
                        Log.v(tag, "Changing username")
                        changeUsername(user!!, name_editText.text.toString(), this, profile_content)
                    }

                    (findViewById<Button>(R.id.emailSave_button)).setOnClickListener {
                        Log.v(tag, "Changing email")
                        changeEmail(user!!, email_editText.text.toString(), this, profile_content)
                    }

                    (findViewById<Button>(R.id.logout_button)).setOnClickListener {
                        Log.v(tag, "Logging out")
                        mAuth.signOut()
                        finish()
                    }

                    Log.v(tag, "First updated ui")
                }

                user = mAuth.currentUser

                Runnable {
                    if (user != null)
                        for (userInfo in user!!.providerData) {
                            val userProfileImageUrl = user!!.photoUrl
                            if (userProfileImageUrl != null) NetworkUtils.setImageViewFromUrl(session_image, userProfileImageUrl, 8000)
                        }
                }.run()

                name_editText.setText(user!!.displayName!!)
                email_editText.setText(user!!.email!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        updateView()

        // TODO: Check if valid user logged in
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data) && user != null) {
            // or get a single image only
            val image = ImagePicker.getFirstImageOrNull(data)
            uploadProfileImage(user!!, image.path, this, profile_content)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
