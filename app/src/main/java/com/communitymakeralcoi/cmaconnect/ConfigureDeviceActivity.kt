package com.communitymakeralcoi.cmaconnect

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import kotlinx.android.synthetic.main.activity_configure_device.*


class ConfigureDeviceActivity : CMAActivity() {
    lateinit var connectNetwork : String

    override fun setupView() { }

    override fun updateView() {
        if (ui_version == 1)
            if (firstLaunch) {
                setContentView(R.layout.activity_configure_device)

                configureDevice_fab.setOnClickListener {
                    configureDevice_progressBar.visibility = View.VISIBLE

                    configureDevice_imageView.visibility = View.GONE
                    configureDeviceWifiAnimation_imageView.visibility = View.VISIBLE

                    val bgImage = configureDeviceWifiAnimation_imageView.background as AnimationDrawable
                    bgImage.start()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent != null){
            connectNetwork = intent.getStringExtra("connectNetwork")
            if(connectNetwork == ""){
                Log.e(tag, "Any network to connect found!")
                finish()
            }
        }else{
            Log.e(tag, "Any intent found!")
            finish()
        }
    }

    private fun connectToWifi(){
        // Declare configuration
        val conf = WifiConfiguration()
        conf.SSID = "\"" + connectNetwork + "\""   // Please note the quotes. String should contain ssid in quotes
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)

        // Add to Android Wifi Manager Settings
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.addNetwork(conf)

        // Connect to that network
        val list = wifiManager.configuredNetworks
        for (i in list) {
            if (i.SSID != null && i.SSID == "\"" + connectNetwork + "\"") {
                wifiManager.disconnect()
                wifiManager.enableNetwork(i.networkId, true)
                wifiManager.reconnect()

                break
            }
        }
    }
}
