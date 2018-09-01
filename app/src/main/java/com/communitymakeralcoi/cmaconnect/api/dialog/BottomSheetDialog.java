package com.communitymakeralcoi.cmaconnect.api.dialog;

import android.app.Activity;
import android.view.View;

import androidx.annotation.LayoutRes;

public abstract class BottomSheetDialog {
    public BottomSheetDialog(Activity activity, @LayoutRes int layoutRes){
        com.google.android.material.bottomsheet.BottomSheetDialog mBottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(layoutRes, null);
        mBottomSheetDialog.setContentView(sheetView);

        setupView(sheetView);

        mBottomSheetDialog.show();
    }

    protected abstract void setupView(View view);
}
