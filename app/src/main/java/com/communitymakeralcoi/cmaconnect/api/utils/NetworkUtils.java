package com.communitymakeralcoi.cmaconnect.api.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.ImageView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NetworkUtils {
    public static void setImageViewFromUrl(ImageView imageView, Uri url, int timeout) throws InterruptedException, ExecutionException, TimeoutException {
        new DownloadImageTask(imageView).get(timeout, TimeUnit.MILLISECONDS);
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
            return cm.getActiveNetworkInfo() != null;
        return false;
    }

    public static boolean isInternetAvailable(){
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.toString().equals("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }
}
