package com.communitymakeralcoi.cmaconnect

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.communitymakeralcoi.cmaconnect.api.utils.PermissionUtils
import android.net.wifi.WifiManager
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.config.wifi_name
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import kotlinx.android.synthetic.main.activity_devices.*


class DevicesActivity : CMAActivity() {
    lateinit var wifiManager: WifiManager
    lateinit var wifiScanReceiver: BroadcastReceiver

    var availableNetworkSSID : String? = null

    override fun setupView() {

    }

    override fun updateView() {
        if (ui_version == 1) {
            if (firstLaunch) {
                setContentView(R.layout.activity_devices)

                newDevicePanel.visibility = View.GONE // Start without showing the configure device panel

                wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                wifiScanReceiver = object : BroadcastReceiver() {
                    override fun onReceive(contxt: Context?, intent: Intent?) {
                        Log.v(tag, "wifiScanReceiver got data")
                        if (intent?.action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                            val mScanResults = wifiManager.scanResults
                            Log.v(tag, "Got ${mScanResults.size} results. Iterating...")
                            // add your logic here
                            for (result in mScanResults) {
                                if (result.SSID.contains(wifi_name, true)) {
                                    Log.w(tag, "Found a device to configure!")

                                    // TODO: Show new device available
                                    availableNetworkSSID = result.SSID
                                    val enterAnimation = AnimationUtils.loadAnimation(this@DevicesActivity, R.anim.enter_from_top)
                                    enterAnimation.setAnimationListener(object : Animation.AnimationListener {
                                        override fun onAnimationRepeat(animation: Animation?) { }

                                        override fun onAnimationEnd(animation: Animation?) { }

                                        override fun onAnimationStart(animation: Animation?) {
                                            newDevicePanel.visibility = View.VISIBLE
                                        }

                                    })
                                }else
                                    Log.v(tag, "Detected network ${result.SSID}")
                            }
                        }else
                            Log.w(tag, "Data not valid")
                    }
                }

                newDeviceConfigure_button.setOnClickListener {
                    if(availableNetworkSSID != null)
                        startActivity(Intent(this@DevicesActivity, ConfigureDeviceActivity().javaClass).putExtra("connectNetwork", availableNetworkSSID))
                    else
                        // TODO: Text from res
                        showToast("Any device detected to configure", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Explain why permission
        PermissionUtils.checkAndAskForPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        PermissionUtils.checkAndAskForPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
        PermissionUtils.checkAndAskForPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    }

    override fun onResume() {
        super.onResume()
        scan()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun scan(){
        if(!wifiManager.isWifiEnabled){
            Log.w(tag, "Wifi is off, turning on")
            wifiManager.isWifiEnabled = true
        }
        Log.v(tag, "Starting network scanner")
        wifiManager.startScan()
    }
}
