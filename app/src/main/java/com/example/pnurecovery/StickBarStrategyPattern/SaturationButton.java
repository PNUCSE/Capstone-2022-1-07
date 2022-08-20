package com.example.pnurecovery.StickBarStrategyPattern;

import android.app.Activity;
import android.graphics.LightingColorFilter;

public class SaturationButton extends StickBarStrategyPattern {
    public SaturationButton(Activity activity) {
        super(activity);
    }

    @Override
    public void buttonOperating(int i) {
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("채도 : " + String.valueOf(i));
    }
}
