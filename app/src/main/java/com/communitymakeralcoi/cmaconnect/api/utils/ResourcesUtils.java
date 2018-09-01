package com.communitymakeralcoi.cmaconnect.api.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

public class ResourcesUtils {
    @ColorInt
    public static int getColor(Context context, @ColorRes int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return context.getResources().getColor(color, context.getTheme());
        else
            return context.getResources().getColor(color);
    }
}
