package com.communitymakeralcoi.cmaconnect

import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import com.communitymakeralcoi.cmaconnect.api.config.sharedPreferences
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.view.AppCompatPreferenceActivity
import android.content.Intent




class SettingsActivity : AppCompatPreferenceActivity() {
    fun setupView() {
        when (ui_version) {
            1 -> {
                setTheme(R.style.Style1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupView()
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // load settings fragment
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment()).commit()
    }

    fun changeFragment(newFragment: android.app.Fragment) {
        fragmentManager.beginTransaction().replace(android.R.id.content, newFragment).commit()
    }

    var hierachyHeight = 0
    class MainPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_main)

            (activity as SettingsActivity).hierachyHeight = 0

            //(activity as SettingsActivity).bindPreferenceSummaryToValue(findPreference(getString(R.string.key_gallery_name)))

            val generalPref = findPreference("pref_general")
            generalPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as SettingsActivity).changeFragment(GeneralPreferenceFragment())
                true
            }
            val notificationsPref = findPreference("pref_notif")
            notificationsPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as SettingsActivity).changeFragment(NotificationsPreferenceFragment())
                true
            }
            val infoPref = findPreference("pref_info")
            infoPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as SettingsActivity).changeFragment(InfoPreferenceFragment())
                true
            }
        }
    }
    class GeneralPreferenceFragment : PreferenceFragment() {
        private fun updateView(){
            consumerCode.text = sharedPreferences.getString("pref_consumer_code", "")
            enableAds.isEnabled = consumerCode.text.isNotEmpty()
        }

        lateinit var consumerCode : EditTextPreference
        lateinit var enableAds : SwitchPreference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)

            (activity as SettingsActivity).hierachyHeight = 1

            consumerCode = findPreference("pref_consumer_code") as EditTextPreference
            enableAds = findPreference("pref_enable_ads") as SwitchPreference
            consumerCode.setOnPreferenceChangeListener { pref, newValue ->
                val preference = pref as EditTextPreference

                // TODO: Check if valid code
                preference.summary = newValue as String
                with(sharedPreferences.edit()){
                    putString("consumer_code", newValue)
                    apply()
                }
                updateView()
                true
            }
            updateView()
        }
    }
    class NotificationsPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_notifications)

            (activity as SettingsActivity).hierachyHeight = 1

            //(activity as SettingsActivity).bindPreferenceSummaryToValue(findPreference(getString(R.string.key_gallery_name)))
            val enableServerNotifications = findPreference("server_notifications_enable") as SwitchPreference
            enableServerNotifications.setOnPreferenceChangeListener { _, newValue ->
                with(sharedPreferences.edit()){
                    putBoolean("server_notifications_enable", newValue as Boolean)
                    apply()
                }
                true
            }
            enableServerNotifications.isChecked = sharedPreferences.getBoolean("server_notifications_enable", true)
        }
    }
    class InfoPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_info)

            (activity as SettingsActivity).hierachyHeight = 1

            //(activity as SettingsActivity).bindPreferenceSummaryToValue(findPreference(getString(R.string.key_gallery_name)))
            val versionPref = findPreference("pref_version")
            val buildPref = findPreference("pref_build")
            val uiVersionPref = findPreference("pref_ui_version")
            versionPref.summary = BuildConfig.VERSION_NAME
            buildPref.summary = BuildConfig.VERSION_CODE.toString()
            uiVersionPref.summary = ui_version.toString()

            val folderPickerPref = findPreference("pref_folder_picker")
            val firebasePref = findPreference("pref_firebase")
            val glidePref = findPreference("pref_glide")
            folderPickerPref.summary = BuildConfig.imagePickerVersion
            firebasePref.summary = BuildConfig.firebaseVersion
            glidePref.summary = BuildConfig.glideVersion
            folderPickerPref.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/esafirm/android-image-picker")))
                true
            }
            firebasePref.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://firebase.google.com")))
                true
            }
            glidePref.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/bumptech/glide")))
                true
            }
        }
    }

    override fun onBackPressed() {
        if(hierachyHeight <= 0) super.onBackPressed()
        else if(hierachyHeight == 1) changeFragment(MainPreferenceFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    public fun bindPreferenceSummaryToValue(preference: Preference) {
        preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.context)
                        .getString(preference.key, ""))
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        val stringValue = newValue.toString()

        if (preference is ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            val index = preference.findIndexOfValue(stringValue)

            // Set the summary to reflect the new value.
            preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null)

        } else if (preference is RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_ringtone_silent)

            } else {
                val ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue))

                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.setSummary(R.string.summary_choose_ringtone)
                } else {
                    // Set the summary to reflect the new ringtone display
                    // name.
                    val name = ringtone.getTitle(preference.getContext())
                    preference.setSummary(name)
                }
            }

        } else if (preference is EditTextPreference) {
            if (preference.getKey() == "key_gallery_name") {
                // update the changed gallery name to summary filed
                preference.setSummary(stringValue)
            }
        } else {
            preference.summary = stringValue
        }
        true
    }
}
