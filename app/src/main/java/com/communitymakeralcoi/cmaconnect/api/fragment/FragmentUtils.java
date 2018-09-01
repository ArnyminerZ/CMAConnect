package com.communitymakeralcoi.cmaconnect.api.fragment;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public static void replaceFragmentWithAnimation(AppCompatActivity appCompatActivity,
                                                    @IdRes int fragment_container,
                                                    @AnimRes int enterAnimation, @AnimRes int exitAnimation,
                                                    @AnimRes int enterPopAnimation, @AnimRes int exitPopAnimation,
                                                    Fragment fragment, String tag){
        FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnimation, exitAnimation, enterPopAnimation, exitPopAnimation);
        transaction.replace(fragment_container, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
