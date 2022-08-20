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

    public Bitmap changeBitmapBrightness(Bitmap bmp, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap change_Bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(change_Bitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return change_Bitmap;
    }

    @Override
    public void buttonOperating(int i) {
        brightness = -127 + 127 * (i/50.0);
        img.setImageBitmap(changeBitmapBrightness(bmp, (float)brightness));
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("밝기 : " + String.valueOf(i));
    }
}