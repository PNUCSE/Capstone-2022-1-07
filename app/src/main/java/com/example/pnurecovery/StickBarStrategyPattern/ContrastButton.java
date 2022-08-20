package com.example.pnurecovery.StickBarStrategyPattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.pnurecovery.R;

public class ContrastButton extends StickBarStrategyPattern {
    double contrast = 1;

    public ContrastButton(Activity activity) {
        super(activity);
    }

    @Override
    public ColorMatrix createColorMatrix(float contrast) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
                        0, 0, 0, 1, 0
                });

        return cm;
    }

    @Override
    public void buttonOperating(int i) {
        contrast = 0 + 1 * (i/50.0);
        img.setImageBitmap(changeBitmap(bmp, (float)contrast));
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("대비 : " + String.valueOf(i));
    }
}