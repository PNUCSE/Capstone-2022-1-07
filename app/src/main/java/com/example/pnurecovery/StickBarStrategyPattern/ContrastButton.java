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

    public Bitmap changeBitmapContrast(Bitmap bmp, float contrast)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
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
        contrast = 0 + 1 * (i/50.0);
        img.setImageBitmap(changeBitmapContrast(bmp, (float)contrast));
    }

    @Override
    public void messageSetting(int i) {
        mode_notification.setText("대비 : " + String.valueOf(i));
    }
}