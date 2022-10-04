package com.example.pnurecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.gpu.GpuDelegateFactory;

import com.bumptech.glide.Glide;
import com.example.pnurecovery.StickBarStrategyPattern.BrightnessButton;
import com.example.pnurecovery.StickBarStrategyPattern.ContrastButton;
import com.example.pnurecovery.StickBarStrategyPattern.SaturationButton;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
    ImageView resultView;


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

        edit_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeStickBar();
                checkTurnOnEditDetail();
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

        Button sr_button = (Button) edit_layer.findViewById(R.id.sr_button);

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
        resultView = findViewById(R.id.imageview);

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
                bit = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, false);

                img.setImageBitmap(bit);
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

                ImageView img;
                Bitmap bmp;

                int origin_width;
                int origin_height;

                img = (ImageView) activity.findViewById(R.id.imageView);

                origin_width = img.getWidth();
                origin_height = img.getHeight();

                img.destroyDrawingCache();
                img.setDrawingCacheEnabled(true);
                bmp = img.getDrawingCache();

                int inputWidth = 50;
                int inputHeight = 50;

                Bitmap bitmap = Bitmap.createScaledBitmap(bmp, inputWidth, inputHeight, true);
                ByteBuffer input = ByteBuffer.allocateDirect(inputWidth * inputHeight * 3 * 4).order(ByteOrder.nativeOrder());
                for (int y = 0; y < inputHeight; y++) {
                    for (int x = 0; x < inputWidth; x++) {
                        int px = bitmap.getPixel(x, y);

                        int r = Color.red(px);
                        int g = Color.green(px);
                        int b = Color.blue(px);

                        input.putFloat((float)r);
                        input.putFloat((float)g);
                        input.putFloat((float)b);
                    }
                }

                int outputWidth = 200;
                int outputHeight = 200;

                ByteBuffer modelOutput = ByteBuffer.allocateDirect(outputWidth * outputHeight * 3 * 4).order(ByteOrder.nativeOrder());

                Interpreter tflite = getTfliteInterpreter("ESRGAN.tflite");
                tflite.run(input, modelOutput);

                modelOutput.rewind();

                Bitmap bitmap2 = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888);

                int [] pixels = new int[outputWidth * outputHeight];
                for (int i = 0; i < outputWidth * outputHeight; i++) {
                    int a = 0xFF;

                    short r = (short)(modelOutput.getFloat());
                    short g = (short)(modelOutput.getFloat());
                    short b = (short)(modelOutput.getFloat());

                    if (r > 250) {
                        r = 0xFF;
                    }
                    if (g > 250) {
                        g = 0xFF;
                    }
                    if (b > 250) {
                        b = 0xFF;
                    }

                    if (r < 6) {
                        r = 0;
                    }
                    if (g < 6) {
                        g = 0;
                    }
                    if (b < 6) {
                        b = 0;
                    }

                    pixels[i] = a << 24 | r << 16 | g << 8 | b;
                }

                bitmap2.setPixels(pixels, 0, outputWidth, 0, 0, outputWidth, outputHeight);
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, origin_width, origin_height, true);

                img.setImageBitmap(bitmap2);
                Glide.with(getApplicationContext()).load(bitmap2).override(2000, 2000).into(imageView);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSave();
            }
        });
    }

    // crop
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select) {
            resultView.setImageDrawable(null);
            Crop.pickImage(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Glide.with(getApplicationContext()).load(result.getData()).override(2000, 2000).into(imageView);
                Toast.makeText(activity, "이미지로드", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                //Glide.with(getApplicationContext()).load(result.getData()).override(500, 500).into(resultView);
                Toast.makeText(activity, "Crop", Toast.LENGTH_SHORT).show();
                beginCrop(result.getData());

            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);

            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
        }
        else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    // crop end

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    // 이미지 로드
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                Glide.with(getApplicationContext()).load(data.getData()).override(1000, 1000).into(imageView);
//
//            }
//        }
//    }
}
