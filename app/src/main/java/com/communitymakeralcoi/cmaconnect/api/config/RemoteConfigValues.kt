package com.communitymakeralcoi.cmaconnect.api.config

val REMOTE_CONFIG_VAR = "RemoteConfigLoader"

val remoteConfigDefaults = mapOf(
        "ui_version" to -1,
        "last_app_version" to "0.0.0.0",
        "last_firmware_version" to "0.0.0.0",
        "google_play_available" to false,
        "wifi_name" to ""
)

var ui_version = -1
var last_app_version = "-1"
var last_firmware_version = "-1"
var google_play_available = false
var wifi_name = ""