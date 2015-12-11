package com.llf.mybitmapapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.llf.mybitmapapp.dialog.DialogImageInformation;
import com.llf.mybitmapapp.util.BitmapUtils;
import com.llf.mybitmapapp.util.ToastUtils;

import java.io.IOException;

public class EditBitmapActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_photo;
    private String filePath;
    private int expectWidth = 0;
    private int expectHeight = 0;
    private Uri contentUri = null;
    private StringBuffer sb;

    private Button btn_editInfo;
    private Button btn_editCombine;
    private Button btn_editSaturation;
    private Button btn_editRotate;
    private Button btn_editZoom;
    private Button btn_editLight;

    private TextView tv_aperture;
    private TextView tv_datetime;
    private TextView tv_exposureTime;
    private TextView tv_flash;
    private TextView tv_focalLength;
    private TextView tv_imageLength;
    private TextView tv_imageWidth;
    private TextView tv_iso;
    private TextView tv_make;
    private TextView tv_model;
    private TextView tv_orientation;

    private DialogImageInformation dialogImageInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bitmap);
        getSupportActionBar().hide();
        filePath = getIntent().getStringExtra("FilePath");
        expectWidth = getIntent().getIntExtra("Width", 600);
        expectHeight = getIntent().getIntExtra("Height", 600);
        initView();
        getExifInformation(filePath);
    }

    private void initView() {
        img_photo = (ImageView) findViewById(R.id.img_photo);
        Bitmap large_bitmap = BitmapUtils.compressBitmap(filePath, expectWidth, expectHeight);
        img_photo.setImageBitmap(large_bitmap);

        btn_editInfo = (Button) findViewById(R.id.btn_editInfo);
        btn_editCombine = (Button) findViewById(R.id.btn_editCombine);
        btn_editSaturation = (Button) findViewById(R.id.btn_editSaturation);
        btn_editRotate = (Button) findViewById(R.id.btn_editRotate);
        btn_editZoom = (Button) findViewById(R.id.btn_editZoom);
        btn_editLight = (Button) findViewById(R.id.btn_editLight);
        btn_editInfo.setOnClickListener(this);
        btn_editCombine.setOnClickListener(this);
        btn_editSaturation.setOnClickListener(this);
        btn_editRotate.setOnClickListener(this);
        btn_editZoom.setOnClickListener(this);
        btn_editLight.setOnClickListener(this);

        tv_aperture = (TextView) findViewById(R.id.tv_aperture);
        tv_datetime = (TextView) findViewById(R.id.tv_datetime);
        tv_exposureTime = (TextView) findViewById(R.id.tv_exposureTime);
        tv_flash = (TextView) findViewById(R.id.tv_flash);
        tv_focalLength = (TextView) findViewById(R.id.tv_focalLength);
        tv_imageLength = (TextView) findViewById(R.id.tv_imageLength);
        tv_imageWidth = (TextView) findViewById(R.id.tv_imageWidth);
        tv_iso = (TextView) findViewById(R.id.tv_iso);
        tv_make = (TextView) findViewById(R.id.tv_make);
        tv_model = (TextView) findViewById(R.id.tv_model);
        tv_orientation = (TextView) findViewById(R.id.tv_orientation);
    }

    private void getExifInformation(String filePath) {

        try {
            ExifInterface exif = new ExifInterface(filePath);
            tv_aperture.setText(exif.getAttribute(ExifInterface.TAG_APERTURE));
            tv_datetime.setText(exif.getAttribute(ExifInterface.TAG_DATETIME));
            tv_exposureTime.setText(exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME));
            tv_flash.setText(exif.getAttribute(ExifInterface.TAG_FLASH));
            tv_focalLength.setText(exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
            tv_imageLength.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            tv_imageWidth.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            tv_iso.setText(exif.getAttribute(ExifInterface.TAG_ISO));
            tv_make.setText(exif.getAttribute(ExifInterface.TAG_MAKE));
            tv_model.setText(exif.getAttribute(ExifInterface.TAG_MODEL));
            int degree = 0;
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
            }
            tv_orientation.setText(degree+"");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Uri getContentUriFromFilePath(String filePath){
        Uri uri;
        if(sb == null) {
            sb = new StringBuffer();
        }
        sb.setLength(0);
        sb.append("(");
        sb.append(MediaStore.Images.ImageColumns.DATA);
        sb.append("=");
        sb.append("'"+filePath+"'");
        sb.append(")");
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID}, sb.toString(), null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

        }
        int id = 0;
        if(cursor.moveToFirst()){
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            id = cursor.getInt(index);
        }
        if(id!=0){
            uri = Uri.parse("content://media/external/images/media/" + id);
        }else{
            uri = null;
        }
        cursor.close();
        return uri;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editInfo:
                contentUri = getContentUriFromFilePath(filePath);
                if(contentUri == null){
                    ToastUtils.toastMessage(EditBitmapActivity.this, "Can Not Get Uri!");
                    return;
                }
                if(dialogImageInformation == null) {
                    dialogImageInformation = new DialogImageInformation(EditBitmapActivity.this, contentUri, filePath);
                }
                dialogImageInformation.showDialog();
                break;
            case R.id.btn_editCombine:

                break;
            case R.id.btn_editSaturation:

                break;
            case R.id.btn_editRotate:

                break;
            case R.id.btn_editZoom:

                break;
            case R.id.btn_editLight:

                break;
        }
    }
}
