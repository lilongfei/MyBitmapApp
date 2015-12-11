package com.llf.mybitmapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.llf.mybitmapapp.dialog.DialogInputName;
import com.llf.mybitmapapp.util.BitmapUtils;
import com.llf.mybitmapapp.util.CommonUtils;
import com.llf.mybitmapapp.util.ToastUtils;
import com.llf.mybitmapapp.util.TypeCamera;

import java.io.File;

public class CaptureBitmapActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_camera;
    private Button btn_previous;
    private Button btn_next;
    private TextView tv_title;
    private ImageButton btn_photo;
    private ImageView img_thumb;
    private DialogInputName dialog_inputName;
    public static final int REQUEST_SDCARD_CAMERA = 1000;
    public static final int REQUEST_MEDIA_STORE_CAMERA = 1001;
    private int expectWidth = 0;
    private int expectHeight = 0;
    private DisplayMetrics dm;
    private String filePath;
    private Intent intent_edit;
    private int typeCamera = TypeCamera.TYPE_CAMERA_DEFAULT;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_bitmap);
        getSupportActionBar().hide();
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        expectWidth = dm.widthPixels;
        expectHeight = dm.heightPixels;

        if (getIntent().getIntExtra("title", R.string.default_camera) == R.string.default_camera) {
            typeCamera = TypeCamera.TYPE_CAMERA_DEFAULT;
        } else {
            typeCamera = TypeCamera.TYPE_CAMERA_CUSTOM;
        }
        initView();
    }

    private void initView() {
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_photo = (ImageButton) findViewById(R.id.btn_photo);
        img_thumb = (ImageView) findViewById(R.id.img_thumb);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getIntExtra("title", R.string.default_camera));

        btn_camera.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.gc();
        switch (requestCode) {
            case REQUEST_SDCARD_CAMERA:
                if (resultCode == RESULT_OK) {
                    filePath = dialog_inputName.getFilePath();
                    BitmapUtils.updateGallery(this, dialog_inputName.getFilePath());
                    Bitmap bitmap = BitmapUtils.compressBitmap(dialog_inputName.getFilePath(), CommonUtils.dip2px(this,100), CommonUtils.dip2px(this,100));
                    img_thumb.setImageBitmap(bitmap);
                    Bitmap large_bitmap = BitmapUtils.compressBitmap(dialog_inputName.getFilePath(), expectWidth, expectHeight);
                    btn_photo.setImageBitmap(large_bitmap);
                } else {
                    ToastUtils.toastMessage(this,R.string.cancel);
                }
                break;
            case REQUEST_MEDIA_STORE_CAMERA:
                /*Note:The code is not combined with filePath here for showing get bitmap from URI*/
                if (resultCode == RESULT_OK) {
                    filePath = BitmapUtils.getPathFromUri(this, dialog_inputName.getUri());
                    Bitmap bitmap = BitmapUtils.compressBitmap(this, dialog_inputName.getUri(), CommonUtils.dip2px(this,100), CommonUtils.dip2px(this,100));
                    img_thumb.setImageBitmap(bitmap);
                    Bitmap large_bitmap = BitmapUtils.compressBitmap(this, dialog_inputName.getUri(), expectWidth, expectHeight);
                    btn_photo.setImageBitmap(large_bitmap);
                } else {
                    ToastUtils.toastMessage(this,R.string.cancel);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                if(dialog_inputName == null) {
                    dialog_inputName = new DialogInputName(CaptureBitmapActivity.this, typeCamera);
                }
                dialog_inputName.showDialog();
                break;
            case R.id.btn_previous:
                break;
            case R.id.btn_next:
                break;
            case R.id.btn_photo:
                if(filePath == null || filePath.isEmpty()){
                    ToastUtils.toastMessage(CaptureBitmapActivity.this, R.string.error_camera_null);
                    return;
                }
                if(intent_edit == null){
                    intent_edit = new Intent();
                    intent_edit.setClass(CaptureBitmapActivity.this, EditBitmapActivity.class);
                }
                intent_edit.putExtra("FilePath", filePath);
                intent_edit.putExtra("Width", expectWidth);
                intent_edit.putExtra("Height", expectHeight);
                startActivity(intent_edit);
                break;
        }
    }
}
