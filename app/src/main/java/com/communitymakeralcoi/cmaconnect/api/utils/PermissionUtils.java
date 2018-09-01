package com.communitymakeralcoi.cmaconnect.api.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static final int PERMISSIONS_REQUEST = 323;

    public static boolean permissionGranted(Context context, String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static void checkAndAskForPermission(Activity activity, String permission){
        if(!permissionGranted(activity, permission))
            ActivityCompat.requestPermissions(activity,
                    new String[]{ permission },
                    PERMISSIONS_REQUEST);
    }
}
