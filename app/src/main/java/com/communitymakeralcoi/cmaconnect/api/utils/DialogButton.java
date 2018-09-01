package com.communitymakeralcoi.cmaconnect.api.utils;

import android.content.DialogInterface;

import androidx.annotation.StringRes;

public class DialogButton {
    private int text;
    private DialogInterface.OnClickListener onClickListener;

    public DialogButton(@StringRes int text, DialogInterface.OnClickListener onClickListener){
        this.text = text;
        this.onClickListener = onClickListener;
    }

    public int getText() {
        return text;
    }
    public DialogInterface.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
