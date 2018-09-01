package com.communitymakeralcoi.cmaconnect.api.utils;

public class FileUtils {
    public static String extensionFromStringPath(String path){
        String[] pathSplit = path.split(".");
        return pathSplit[pathSplit.length - 1];
    }
}
