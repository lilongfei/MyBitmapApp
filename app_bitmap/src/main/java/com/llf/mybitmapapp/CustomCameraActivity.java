package com.llf.mybitmapapp;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.llf.mybitmapapp.dialog.DialogCameraSetting;
import com.llf.mybitmapapp.util.IntentExtraKey;
import com.llf.mybitmapapp.util.ToastUtils;
import com.llf.mybitmapapp.util.TypeCamera;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;


public class CustomCameraActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.PictureCallback {

    private Uri uri;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private TextView tv_time;
    private Button btn_no;
    private Button btn_ok;
    private Button btn_shoot;
    private Button btn_setting;
    private LinearLayout ll_shootNext;
    private byte[] pictureData;
    private DialogCameraSetting dialogCameraSetting;
    private int delay = 0;
    private int delayTemp = 0;
    private int flashMode = TypeCamera.FLASHLIGHT_CLOSE;
    private int focusMode = TypeCamera.FOCUS_AUTO;
    private int sceneMode = TypeCamera.SCENE_AUTO;
    private int colorEffectMode = TypeCamera.COLOR_EFFECT_NONE;
    private int cameraID = TypeCamera.CAMERA_ID_BACK;

    private Handler delayShootHandler = new Handler();
    private boolean isDelayTimerRunning = false;
    private Runnable delayTimer = new Runnable() {
        @Override
        public void run() {
            if (delayTemp > 0) {
                if(delayTemp >0&&delayTemp<4){
                    ringAlarm();
                }
                tv_time.setText(delayTemp+"");
                delayTemp--;
                delayShootHandler.postDelayed(delayTimer, 1000);
            } else {
                camera.takePicture(null, null, null, CustomCameraActivity.this);
                isDelayTimerRunning = false;
                tv_time.setVisibility(View.INVISIBLE);
                delayTemp = delay;
                btn_shoot.setText(R.string.shoot);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        getSupportActionBar().hide();
        uri = getIntent().getParcelableExtra(IntentExtraKey.KEY_URI);
        initView();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        btn_no = (Button) findViewById(R.id.btn_no);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_shoot = (Button) findViewById(R.id.btn_shoot);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_no.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_shoot.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        ll_shootNext = (LinearLayout) findViewById(R.id.ll_shootNext);
        ll_shootNext.setVisibility(View.INVISIBLE);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setVisibility(View.INVISIBLE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("MMMM", "surfaceCreated");
        camera = Camera.open(cameraID);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
            camera.release();
        }
        settingFlashMode(flashMode);
        settingFocusMode(focusMode);
        settingSceneMode(sceneMode);
        settingColorEffectMode(colorEffectMode);
        Camera.Parameters params = camera.getParameters();
        camera.setDisplayOrientation(90);
        if(cameraID == TypeCamera.CAMERA_ID_BACK){
            params.setRotation(90);
        }else{
            params.setRotation(270);
        }
        camera.setParameters(params);
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("MMMM", "surfaceChanged");
        Camera.Parameters params = camera.getParameters();
        camera.setDisplayOrientation(90);
        if(cameraID == TypeCamera.CAMERA_ID_BACK){
            params.setRotation(90);
        }else{
            params.setRotation(270);
        }
        camera.setParameters(params);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MMMM", "surfaceDestroyed");
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        ringTakePicture();
        camera.stopPreview();
        pictureData = data;
        ll_shootNext.setVisibility(View.VISIBLE);
        btn_shoot.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no:
                restartPreview();
                break;
            case R.id.btn_ok:
                /*save picture and setResult&finish*/
                savePicture(uri, pictureData);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_shoot:
                /*take picture*/
                doTakePicture();
                break;
            case R.id.btn_setting:
                if(btn_shoot.getVisibility() == View.GONE){
                    ToastUtils.toastMessage(CustomCameraActivity.this, R.string.error_camera_setting);
                    return;
                }
                if (dialogCameraSetting == null) {
                    dialogCameraSetting = new DialogCameraSetting(CustomCameraActivity.this, camera);
                }
                dialogCameraSetting.showDialog();
                dialogCameraSetting.setParameters(delay, flashMode, focusMode, sceneMode, colorEffectMode, cameraID);
                break;
        }
    }

    private void doTakePicture() {
        if (delay == 0) {
            camera.takePicture(null, null, null, this);
        } else {
            if (!isDelayTimerRunning) {
                isDelayTimerRunning = true;
                btn_shoot.setText(R.string.cancel);
                delayTemp = delay;
                tv_time.setVisibility(View.VISIBLE);
                delayShootHandler.post(delayTimer);
            } else {
                delayShootHandler.removeCallbacks(delayTimer);
                isDelayTimerRunning = false;
                tv_time.setVisibility(View.INVISIBLE);
                delayTemp = delay;
                btn_shoot.setText(R.string.shoot);
            }
        }
    }

    public void setting(int delay, int flashMode, int focusMode, int sceneMode, int colorEffectMode, int cameraID) {
        camera.stopPreview();
        this.delay = delay;
        settingDuoCamera(cameraID);
        settingFlashMode(flashMode);
        settingFocusMode(focusMode);
        settingSceneMode(sceneMode);
        settingColorEffectMode(colorEffectMode);
        Camera.Parameters params = camera.getParameters();
        camera.setDisplayOrientation(90);
        if(cameraID == TypeCamera.CAMERA_ID_BACK){
            params.setRotation(90);
        }else{
            params.setRotation(270);
        }
        camera.setParameters(params);
        camera.startPreview();
    }

    private void settingDuoCamera(int cameraID) {
        if(this.cameraID!=cameraID){
            camera.release();
            this.cameraID = cameraID;
            camera = Camera.open(cameraID);
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
                camera.release();
            }
        }
    }

    private void settingFlashMode(int flashMode) {
        this.flashMode = flashMode;
//        if(cameraID!=TypeCamera.CAMERA_ID_BACK){
//            return;
//        }
        Camera.Parameters parameters = camera.getParameters();
        switch (flashMode) {
            case TypeCamera.FLASHLIGHT_CLOSE:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case TypeCamera.FLASHLIGHT_OPEN:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case TypeCamera.FLASHLIGHT_AUTO:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case TypeCamera.FLASHLIGHT_TORCH:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
        }
        camera.setParameters(parameters);
    }

    private void settingFocusMode(int focusMode) {
        this.focusMode = focusMode;
        if(cameraID!=TypeCamera.CAMERA_ID_BACK){
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        switch (focusMode) {
            case TypeCamera.FOCUS_AUTO:
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                break;
            case TypeCamera.FOCUS_MACRO:
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                break;
            case TypeCamera.FOCUS_CONTINUOUS_VIDEO:
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                break;
            case TypeCamera.FOCUS_CONTINUOUS_PICTURE:
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                break;
        }
        camera.setParameters(parameters);
    }

    private void settingSceneMode(int sceneMode) {
        this.sceneMode = sceneMode;
        Camera.Parameters parameters = camera.getParameters();
        switch (sceneMode) {
            case TypeCamera.SCENE_AUTO:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
                break;
            case TypeCamera.SCENE_SNOW:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_SNOW);
                break;
            case TypeCamera.SCENE_NIGHT:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
                break;
            case TypeCamera.SCENE_PARTY:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_PARTY);
                break;
            case TypeCamera.SCENE_BEACH:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_BEACH);
                break;
            case TypeCamera.SCENE_THEATRE:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_THEATRE);
                break;
            case TypeCamera.SCENE_CANDLELIGHT:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_CANDLELIGHT);
                break;
            case TypeCamera.SCENE_SUNSET:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_SUNSET);
                break;
            case TypeCamera.SCENE_FIREWORKS:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_FIREWORKS);
                break;
            case TypeCamera.SCENE_SPORTS:
                parameters.setSceneMode(Camera.Parameters.SCENE_MODE_SPORTS);
                break;
        }
        camera.setParameters(parameters);
    }

    private void settingColorEffectMode(int colorEffectMode) {
        this.colorEffectMode = colorEffectMode;
        Camera.Parameters parameters = camera.getParameters();
        switch (colorEffectMode) {
            case TypeCamera.COLOR_EFFECT_NONE:
                parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
                break;
            case TypeCamera.COLOR_EFFECT_MONO:
                parameters.setColorEffect(Camera.Parameters.EFFECT_MONO);
                break;
            case TypeCamera.COLOR_EFFECT_NEGATIVE:
                parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
                break;
            case TypeCamera.COLOR_EFFECT_SOLARIZE:
                parameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                break;
            case TypeCamera.COLOR_EFFECT_SEPIA:
                parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
                break;
            case TypeCamera.COLOR_EFFECT_POSTERIZE:
                parameters.setColorEffect(Camera.Parameters.EFFECT_POSTERIZE);
                break;
            case TypeCamera.COLOR_EFFECT_WHITEBOARD:
                parameters.setColorEffect(Camera.Parameters.EFFECT_WHITEBOARD);
                break;
            case TypeCamera.COLOR_EFFECT_BLACKBOARD:
                parameters.setColorEffect(Camera.Parameters.EFFECT_BLACKBOARD);
                break;
            case TypeCamera.COLOR_EFFECT_AQUA:
                parameters.setColorEffect(Camera.Parameters.EFFECT_AQUA);
                break;
        }
        camera.setParameters(parameters);
    }

    private void restartPreview() {
        ll_shootNext.setVisibility(View.GONE);
        btn_shoot.setVisibility(View.VISIBLE);
        pictureData = null;
        settingFlashMode(flashMode);
        settingFocusMode(focusMode);
        settingSceneMode(sceneMode);
        settingColorEffectMode(colorEffectMode);
        Camera.Parameters params = camera.getParameters();
        camera.setDisplayOrientation(90);
        if(cameraID == TypeCamera.CAMERA_ID_BACK){
            params.setRotation(90);
        }else{
            params.setRotation(270);
        }
        camera.setParameters(params);
        camera.startPreview();
    }


    private void savePicture(Uri uri, byte[] data) {
        if (data == null) {
            ToastUtils.toastMessage(this, R.string.error_camera_data);
            restartPreview();
            return;
        }
        try {
            OutputStream os = getContentResolver().openOutputStream(uri);
            os.write(data);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ringAlarm() {
        try{
            AudioManager mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mAudioMgr.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }catch(Exception e){

        }
    }

    private void ringTakePicture() {
        try{
            AudioManager mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mAudioMgr.playSoundEffect(AudioManager.FX_KEY_CLICK);
        }catch(Exception e){

        }
    }
}
