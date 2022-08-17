package com.example.pnurecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Context context = (Context)this;
    int degree = 0;
    int edit_on = 0;
    int edit_detail_on = 0;
    int save_count = 0;

    public Uri image_save() {
        ImageView img = (ImageView) findViewById(R.id.imageView);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String getTime = sdf.format(date);

        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = img.getDrawingCache();

        String str = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, getTime + "_" + save_count, "");

        print_image_save_message(getTime + "_" + save_count);
        save_count = save_count + 1;
        img.destroyDrawingCache();

        return Uri.parse(str);
    }

    public void print_image_save_message(String fileName) {
        Toast message = Toast.makeText(context.getApplicationContext(), "picture 폴더에" + fileName + "이 저장되었습니다.", Toast.LENGTH_LONG);
        message.show();
    }

    public void check_turn_on_edit() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        if (edit_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit, edit_layer, true);

            set_edit_button();

            edit_on = 1;
        }
    }

    public void check_turn_off_edit() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        if (edit_on == 1) {
            edit_layer.removeAllViews();

            edit_on = 0;
        }
    }

    public void set_edit_button() {
        LinearLayout edit_layer = (LinearLayout) findViewById(R.id.edit_layout);

        Button edit_detail_button = (Button) edit_layer.findViewById(R.id.edit_detail_button);
        Button cut_button = (Button) edit_layer.findViewById(R.id.cut_button);

        edit_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_on_edit_detail();
            }
        });

        cut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_off_edit_detail();
            }
        });
    }

    public void check_turn_on_edit_detail() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);

        if (edit_detail_on == 0) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.edit_detail, edit_detail_layer, true);

            set_edit_detail_button();

            edit_detail_on = 1;
        }
    }

    public void check_turn_off_edit_detail() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);

        if (edit_detail_on == 1) {
            edit_detail_layer.removeAllViews();

            edit_detail_on = 0;
        }
    }

    public void set_edit_detail_button() {
        LinearLayout edit_detail_layer = (LinearLayout) findViewById(R.id.edit_detail_layout);

        ImageButton brightness_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_brightness);
        ImageButton contrast_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_contrast);
        ImageButton saturation_button = (ImageButton) edit_detail_layer.findViewById(R.id.Button_saturation);

        TextView mode_notification = (TextView) edit_detail_layer.findViewById(R.id.mode_notification);

        brightness_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("밝기");
            }
        });

        contrast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("대비");
            }
        });

        saturation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_notification.setText("채도");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = (ImageView) findViewById(R.id.imageView);
        Button edit_button = (Button) findViewById(R.id.image_edit_button);
        Button turn_button = (Button) findViewById(R.id.image_turn_button);
        Button share_button = (Button) findViewById(R.id.image_share_button);
        Button load_button = (Button) findViewById(R.id.image_load_button);
        Button super_resolution_button = (Button) findViewById(R.id.image_super_resolution_button);
        Button save_button = (Button) findViewById(R.id.image_save_button);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_on_edit();
            }
        });

        turn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_off_edit();
                check_turn_off_edit_detail();

                degree = degree + 90;
                Matrix matrix = new Matrix();

                matrix.postRotate(degree);
                Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.testpicture);

                img.setImageBitmap(Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true));
            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_off_edit();
                check_turn_off_edit_detail();
                Intent share = new Intent(Intent.ACTION_SEND);

                Uri uri = image_save();

                share.setType("image/png");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share image using"));
            }
        });

        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_off_edit();
                check_turn_off_edit_detail();
            }
        });

        super_resolution_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_turn_off_edit();
                check_turn_off_edit_detail();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_save();
            }
        });
    }
}