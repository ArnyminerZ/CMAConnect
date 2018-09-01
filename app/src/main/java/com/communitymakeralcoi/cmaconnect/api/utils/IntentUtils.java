package com.communitymakeralcoi.cmaconnect.api.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {
    public static void openPlayStore(Context context){
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
