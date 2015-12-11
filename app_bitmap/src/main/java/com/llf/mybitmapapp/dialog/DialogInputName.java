
package com.llf.mybitmapapp.dialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.llf.mybitmapapp.CaptureBitmapActivity;
import com.llf.mybitmapapp.CustomCameraActivity;
import com.llf.mybitmapapp.R;
import com.llf.mybitmapapp.util.CommonUtils;
import com.llf.mybitmapapp.util.IntentExtraKey;
import com.llf.mybitmapapp.util.ToastUtils;
import com.llf.mybitmapapp.util.TypeCamera;

import java.io.File;

/**
 * The dialog for Setting Email Address
 */
public class DialogInputName extends DialogView implements OnClickListener {
    private View btn_cancel;
    private EditText et_name;
    private View btn_sdCard;
    private View btn_mediaStore;
    private Context context;
    private StringBuffer default_filePath;
    private Uri uri;
    private int typeCamera;

    public DialogInputName(Context context, int typeCamera) {
        super(context, R.layout.dialog_input_name);
        this.context = context;
        this.typeCamera = typeCamera;
    }

    @Override
    public void showDialog() {
        super.showBegin(true);
        btn_cancel = super.dialog.findViewById(R.id.btn_cancel);
        btn_sdCard = super.dialog.findViewById(R.id.rl_sdCard);
        btn_mediaStore = super.dialog.findViewById(R.id.rl_mediaStore);
        btn_cancel.setOnClickListener(this);
        btn_sdCard.setOnClickListener(this);
        btn_mediaStore.setOnClickListener(this);
        et_name = (EditText) super.dialog.findViewById(R.id.et_name);
        super.showEnd();
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_cancel:
                super.closeDialog(false);
                break;
            case R.id.rl_sdCard:
                sdCardCamera();
                break;
            case R.id.rl_mediaStore:
                mediaStoreCamera();
                break;
        }
    }

    private void sdCardCamera() {
        if (et_name.getText().toString().isEmpty()) {
            ToastUtils.toastMessage(context, R.string.please_input_name);
            return;
        }
        if (default_filePath == null) {
            default_filePath = new StringBuffer();
        }
        default_filePath.setLength(0);
        //>=sdk 2.2 level 8
        default_filePath.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
//        //< sdk 2.2 level 8
//        default_filePath.append(Environment.getExternalStorageDirectory().getAbsolutePath());
//        //relate with the app, if app is uninstall, the picture will be deleted.
//        default_filePath.append(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        default_filePath.append("/");
        default_filePath.append(et_name.getText().toString());//TODO CHECK NAME
        default_filePath.append(".jpg");
        File file = new File(default_filePath.toString());
        Uri uri = Uri.fromFile(file);
        if(typeCamera == TypeCamera.TYPE_CAMERA_DEFAULT){
            Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            ((Activity) context).startActivityForResult(intent_camera, CaptureBitmapActivity.REQUEST_SDCARD_CAMERA);
        }else if(typeCamera == TypeCamera.TYPE_CAMERA_CUSTOM){
            Intent intent_camera = new Intent();
            intent_camera.setClass(context, CustomCameraActivity.class);
            intent_camera.putExtra(IntentExtraKey.KEY_URI, uri);
            ((Activity) context).startActivityForResult(intent_camera, CaptureBitmapActivity.REQUEST_SDCARD_CAMERA);
        }else{
            ToastUtils.toastMessage(context, R.string.error_camera_type);
        }

        super.closeDialog(false);
    }

    private void mediaStoreCamera() {
        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        if(typeCamera == TypeCamera.TYPE_CAMERA_DEFAULT){
            Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            ((Activity) context).startActivityForResult(intent_camera, CaptureBitmapActivity.REQUEST_MEDIA_STORE_CAMERA);
        }else if(typeCamera == TypeCamera.TYPE_CAMERA_CUSTOM){
            Intent intent_camera = new Intent();
            intent_camera.setClass(context, CustomCameraActivity.class);
            intent_camera.putExtra(IntentExtraKey.KEY_URI, uri);
            ((Activity) context).startActivityForResult(intent_camera, CaptureBitmapActivity.REQUEST_MEDIA_STORE_CAMERA);
        }else{
            ToastUtils.toastMessage(context, R.string.error_camera_type);
        }
        super.closeDialog(false);
    }

    public String getFilePath() {
        return default_filePath == null ? null : default_filePath.toString();
    }

    public Uri getUri() {
        return uri;
    }

}
