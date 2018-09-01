package com.communitymakeralcoi.cmaconnect

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.communitymakeralcoi.cmaconnect.api.config.*
import com.communitymakeralcoi.cmaconnect.api.notifications.Utils
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_load.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import android.content.pm.PackageManager
import android.R.attr.versionName
import android.content.pm.PackageInfo
import android.os.Handler
import com.communitymakeralcoi.cmaconnect.api.utils.ComparationUtils.versionCompare
import java.util.*


class LoadActivity : CMAActivity() {

    val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings)
        mFirebaseRemoteConfig.setDefaults(remoteConfigDefaults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var cacheExpiration: Long = 3600 // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (firebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) cacheExpiration = 0

        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoadActivity, "Fetch Succeeded",
                                Toast.LENGTH_SHORT).show()

                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        firebaseRemoteConfig.activateFetched()

                        Log.i(REMOTE_CONFIG_VAR, "Fetched values successfully")

                        ui_version = mFirebaseRemoteConfig.getDouble("ui_version").toInt()
                        last_app_version = mFirebaseRemoteConfig.getString("last_app_version")
                        last_firmware_version = mFirebaseRemoteConfig.getString("last_firmware_version")
                        google_play_available = mFirebaseRemoteConfig.getBoolean("google_play_available")
                        wifi_name = mFirebaseRemoteConfig.getString("wifi_name")

                        Log.v(REMOTE_CONFIG_VAR, "ui_version: $ui_version")
                        Log.v(REMOTE_CONFIG_VAR, "last_app_version: $last_app_version")
                        Log.v(REMOTE_CONFIG_VAR, "last_firmware_version: $last_firmware_version")
                        Log.v(REMOTE_CONFIG_VAR, "google_play_available: $google_play_available")
                        Log.v(REMOTE_CONFIG_VAR, "wifi_name: $wifi_name")

                        setupView()
                        // super.onCreate(savedInstanceState) This should be called outside onSuccessListener
                        updateView()

                        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Utils.createNotificationChannel(this,
                                    getString(R.string.fcm_notification_channel_id),
                                    getString(R.string.fcm_notification_channel_name),
                                    getString(R.string.fcm_notification_channel_desc),
                                    NotificationManager.IMPORTANCE_DEFAULT)
                            with(sharedPreferences.edit()) {
                                putString("fcm_notification_channel_id", getString(R.string.fcm_notification_channel_id))
                                apply()
                            }
                        }
                        startActivity(Intent(this@LoadActivity, MainActivity().javaClass).putExtra("loaded", true))
                    } else {
                        Toast.makeText(this@LoadActivity, "Fetch Failed", Toast.LENGTH_SHORT).show()
                        // TODO: Fetch failed error
                    }
                }
    }

    override fun setupView() {
        // This should change app theme, but LoadActivity uses default style
    }

    override fun updateView() {
        when (ui_version) {
            1 -> {
                setContentView(R.layout.activity_load)

                //<editor-fold desc="New Version available checker">
                var runningOldVersion = false
                try {
                    val pInfo = this.packageManager.getPackageInfo(packageName, 0)
                    val currentVersion = pInfo.versionName

                    val comparedVersion = versionCompare(currentVersion, last_app_version)
                    Log.v(tag, "comparedVersion is $comparedVersion")
                    if (comparedVersion < 0) {
                        Log.v(tag, "App is not updated")
                        runningOldVersion = true
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                //</editor-fold>

                val intent = Intent(this, MainActivity().javaClass)
                intent.putExtra("redirectLoad", false)
                        .putExtra("runningOldVersion", runningOldVersion)
                startActivity(intent)
            }
            else -> {
                Log.v(tag,"Any layout is showing since ui version is $ui_version")
            }
        }
    }
}
