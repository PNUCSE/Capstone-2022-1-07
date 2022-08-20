package com.example.pnurecovery.StickBarStrategyPattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class BrightnessButton extends StickBarStrategyPattern {
    double brightness = 1;

    public BrightnessButton(Activity activity) {
        super(activity);
    }

    @Override
    public ColorMatrix createColorMatrix(float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });

        return cm;
    }

    @Override
    public void buttonOperating(int i) {
        brightness = -127 + 127 * (i/50.0);
        img.setImageBitmap(changeBitmap(bmp, (float)brightness));
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("밝기 : " + String.valueOf(i));
    }
}