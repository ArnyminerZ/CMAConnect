package com.communitymakeralcoi.cmaconnect.api.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.communitymakeralcoi.cmaconnect.R;
import com.communitymakeralcoi.cmaconnect.api.config.RemoteConfigValuesKt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class CMAActivity extends AppCompatActivity {
    protected abstract void setupView();
    public abstract void updateView();

    protected boolean firstLaunch = true;

    protected String tag = CMAActivity.this.getClass().getSimpleName();

    private void _updateView(){
        updateView();
        firstLaunch = false;
    }
    private void _setupView(){
        switch (RemoteConfigValuesKt.getUi_version()){
            case 1: {
                setTheme(R.style.Style1_NoTitle);
                break;
            }
        }
        setupView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setupView();
        super.onCreate(savedInstanceState);
        _updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    protected Toast toast(String text, int duration){
        return Toast.makeText(this, text, duration);
    }
    protected Toast toast(int resId, int duration){
        return Toast.makeText(this, resId, duration);
    }

    protected void showToast(String text, int duration){
        toast(text, duration).show();
    }
    protected void showToast(int resId, int duration){
        toast(resId, duration).show();
    }
}
