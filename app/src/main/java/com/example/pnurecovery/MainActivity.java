package com.example.pnurecovery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pnurecovery.StickBarStrategyPattern.BrightnessButton;
import com.example.pnurecovery.StickBarStrategyPattern.ContrastButton;
import com.example.pnurecovery.StickBarStrategyPattern.SaturationButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Activity activity = (Activity)this;
    Context context = (Context)this;
    int degree = 0;
    int edit_on = 0;
    int edit_detail_on = 0;
    int save_count = 0;
    int stick_bar_on = 0;
    ImageView imageView;

    public Uri imageSave() {
        ImageView img = (ImageView) findViewById(R.id.imageView);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String getTime = sdf.format(date);

        img.destroyDrawingCache();
        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = img.getDrawingCache();

        String str = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, getTime + "_" + save_count, "");

        printImageSaveMessage(getTime + "_" + save_count);
        save_count = save_count + 1;
        img.destroyDrawingCache();

        return Uri.parse(str);
    }

    public void printImageSaveMessage(String fileName) {
        Toast message = Toast.makeText(context.getApplicationContext(), "picture 폴더에" + fileName + "이 저장되었습니다.", Toast.LENGTH_LONG);
        message.show();
    }

    public void check_turn_on_edit() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        if (edit_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit, edit_layer, true);

            setEditButton();

            edit_on = 1;
        }
    }

    public void checkTurnOffEdit() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        if (edit_on == 1) {
            edit_layer.removeAllViews();

            edit_on = 0;
        }
    }

    public void setEditButton() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        Button edit_detail_button = (Button) edit_layer.findViewById(R.id.edit_detail_button);
        Button cut_button = (Button) edit_layer.findViewById(R.id.cut_button);

        edit_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeStickBar();
                checkTurnOnEditDetail();
            }
        });

        cut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeStickBar();
                checkTurnOffEditDetail();
            }
        });
    }

    public void checkTurnOnEditDetail() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);

        if (edit_detail_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit_detail, edit_detail_layer, true);

            setEditDetailButton();

            edit_detail_on = 1;
        }
    }

    public void checkTurnOffEditDetail() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);

        if (edit_detail_on == 1) {
            edit_detail_layer.removeAllViews();

            edit_detail_on = 0;
        }
    }

    public void setEditDetailButton() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);
        LinearLayout edit_detail_stick_layer = (LinearLayout) findViewById(R.id.edit_detail_stick_layout);

        ImageButton brightness_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_brightness);
        ImageButton contrast_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_contrast);
        ImageButton saturation_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_saturation);

        TextView mode_notification = (TextView) edit_detail_layer.findViewById(R.id.mode_notification);


        brightness_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("밝기 : 50");
                removeStickBar();
                inflateStickBar();
                SeekBar seek = (SeekBar) edit_detail_stick_layer.findViewById(R.id.seekBar);
                seek.setOnSeekBarChangeListener(new BrightnessButton(activity));
            }
        });

        contrast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("대비 : 50");
                removeStickBar();
                inflateStickBar();
                SeekBar seek = (SeekBar) edit_detail_stick_layer.findViewById(R.id.seekBar);
                seek.setOnSeekBarChangeListener(new ContrastButton(activity));
            }
        });

        saturation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("채도 : 50");
                removeStickBar();
                inflateStickBar();
                SeekBar seek = (SeekBar) edit_detail_stick_layer.findViewById(R.id.seekBar);
                seek.setOnSeekBarChangeListener(new SaturationButton(activity));
            }
        });
    }

    public void inflateStickBar() {
        LinearLayout edit_detail_stick_layer = (LinearLayout) findViewById(R.id.edit_detail_stick_layout);

        if (stick_bar_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit_detail_stick_bar, edit_detail_stick_layer, true);

            stick_bar_on = 1;
        }
    }

    public void removeStickBar() {
        LinearLayout edit_detail_stick_layer = (LinearLayout) findViewById(R.id.edit_detail_stick_layout);

        if (stick_bar_on == 1) {
            edit_detail_stick_layer.removeAllViews();

            stick_bar_on = 0;
        }
    }

    public void turnOnSRButton() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        if (edit_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.sr_edit, edit_layer, true);

            setSRButton();

            edit_on = 1;
        }
    }

    public void setSRButton() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        Button cartoonization_button = (Button) edit_layer.findViewById(R.id.cartoonization_button);
        Button sr_button = (Button) edit_layer.findViewById(R.id.sr_button);

        cartoonization_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void turnOffAllAdditionalButton() {
        checkTurnOffEdit();
        checkTurnOffEditDetail();
        removeStickBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button edit_button = (Button) findViewById(R.id.image_edit_button);
        Button turn_button = (Button) findViewById(R.id.image_turn_button);
        Button share_button = (Button) findViewById(R.id.image_share_button);
        Button load_button = (Button) findViewById(R.id.image_load_button);
        Button super_resolution_button = (Button) findViewById(R.id.image_super_resolution_button);
        Button save_button = (Button) findViewById(R.id.image_save_button);

        imageView = findViewById(R.id.imageView);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffAllAdditionalButton();
                check_turn_on_edit();
            }
        });

        turn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) findViewById(R.id.imageView);

                turnOffAllAdditionalButton();

                Matrix matrix = new Matrix();

                degree = 90;

                matrix.postRotate(degree);
                img.destroyDrawingCache();
                img.setDrawingCacheEnabled(true);
                Bitmap bit = img.getDrawingCache();

                img.setImageBitmap(Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, false));
            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffAllAdditionalButton();
                Intent share = new Intent(Intent.ACTION_SEND);

                Uri uri = imageSave();

                share.setType("image/png");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share image using"));
            }
        });

        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffAllAdditionalButton();
                
                // 갤러리 접근
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });

        super_resolution_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffAllAdditionalButton();
                turnOnSRButton();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSave();
            }
        });
    }

    // 이미지 로드
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Glide.with(getApplicationContext()).load(data.getData()).override(1000, 1000).into(imageView);
            }
        }
    }

}