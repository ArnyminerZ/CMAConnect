package com.communitymakeralcoi.cmaconnect.api.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;

import com.communitymakeralcoi.cmaconnect.R;
import com.communitymakeralcoi.cmaconnect.api.config.RemoteConfigValuesKt;
import com.communitymakeralcoi.cmaconnect.api.utils.DialogButton;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.view.ContextThemeWrapper;

public class Dialog {
    private AlertDialog dialog;

    public Dialog(Context context, @StringRes int title, @StringRes int message,
                  @Nullable DialogButton positiveButton, @Nullable DialogButton negativeButton, @Nullable DialogButton neutralButton) {
        AlertDialog.Builder dialogBuilder;
        if (RemoteConfigValuesKt.getUi_version() == 1)
            // TODO: Dialog Theme. dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Style1_Dialog));
            dialogBuilder = new AlertDialog.Builder(context);
        else
            dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title).setMessage(message);
        if (positiveButton != null)
            dialogBuilder.setPositiveButton(positiveButton.getText(), positiveButton.getOnClickListener());
        if (negativeButton != null)
            dialogBuilder.setPositiveButton(negativeButton.getText(), negativeButton.getOnClickListener());
        if (neutralButton != null)
            dialogBuilder.setPositiveButton(neutralButton.getText(), neutralButton.getOnClickListener());
        dialog = dialogBuilder.create();
        //if (RemoteConfigValuesKt.getUi_version() == 1) Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 50, 20, 20, 50));
    }
    public Dialog(Context context, String title, String message,
                  @Nullable DialogButton positiveButton, @Nullable DialogButton negativeButton, @Nullable DialogButton neutralButton) {
        AlertDialog.Builder dialogBuilder;
        if (RemoteConfigValuesKt.getUi_version() == 1)
            // TODO: Dialog Theme. dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Style1_Dialog));
            dialogBuilder = new AlertDialog.Builder(context);
        else
            dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title).setMessage(message);
        if (positiveButton != null)
            dialogBuilder.setPositiveButton(positiveButton.getText(), positiveButton.getOnClickListener());
        if (negativeButton != null)
            dialogBuilder.setPositiveButton(negativeButton.getText(), negativeButton.getOnClickListener());
        if (neutralButton != null)
            dialogBuilder.setPositiveButton(neutralButton.getText(), neutralButton.getOnClickListener());
        dialog = dialogBuilder.create();
        //if (RemoteConfigValuesKt.getUi_version() == 1) Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 50, 20, 20, 50));
    }

    public void show(){
        dialog.show();
    }
}
