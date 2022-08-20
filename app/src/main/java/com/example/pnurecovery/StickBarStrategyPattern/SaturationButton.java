package com.example.pnurecovery.StickBarStrategyPattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class SaturationButton extends StickBarStrategyPattern {
    double saturation = 1;

    public SaturationButton(Activity activity) {
        super(activity);
    }

    @Override
    public ColorMatrix createColorMatrix(float saturation) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);

        return cm;
    }

    @Override
    public void buttonOperating(int i) {
        saturation = 0 + 1 * (i/50.0);
        img.setImageBitmap(changeBitmap(bmp, (float)saturation));
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("채도 : " + String.valueOf(i));
    }
}
