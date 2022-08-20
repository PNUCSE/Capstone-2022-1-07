package com.example.pnurecovery.StickBarStrategyPattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pnurecovery.R;

public abstract class StickBarStrategyPattern extends Activity implements SeekBar.OnSeekBarChangeListener {
    Activity activity;

    LinearLayout edit_detail_layer;
    LinearLayout edit_detail_stick_layer;
    TextView mode_notification;
    SeekBar seekBar;
    ImageView img;
    Bitmap bmp;

    public StickBarStrategyPattern(Activity activity) {
        this.activity = activity;

        edit_detail_layer = (LinearLayout) activity.findViewById(R.id.edit_detail_layout);
        edit_detail_stick_layer = (LinearLayout) activity.findViewById(R.id.edit_detail_stick_layout);
        mode_notification = (TextView) edit_detail_layer.findViewById(R.id.mode_notification);
        seekBar = (SeekBar) activity.findViewById(R.id.seekBar);
        img = (ImageView) activity.findViewById(R.id.imageView);

        seekBar.setProgress(50);

        img.destroyDrawingCache();
        img.setDrawingCacheEnabled(true);
        bmp = img.getDrawingCache();
    }

    public Bitmap changeBitmap(Bitmap bmp, float value)
    {
        ColorMatrix cm = createColorMatrix(value);

        Bitmap change_Bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(change_Bitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return change_Bitmap;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        messageSetting(i);
        buttonOperating(i);
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public abstract ColorMatrix createColorMatrix(float value);
    public abstract void buttonOperating(int i);
    public abstract void messageSetting(int i);
}
