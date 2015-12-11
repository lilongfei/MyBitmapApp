package com.llf.mybitmapapp.dialog;

import android.content.Context;
import android.hardware.Camera;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.llf.mybitmapapp.CustomCameraActivity;
import com.llf.mybitmapapp.R;
import com.llf.mybitmapapp.util.CommonUtils;
import com.llf.mybitmapapp.util.ToastUtils;
import com.llf.mybitmapapp.util.TypeCamera;

import java.util.Iterator;
import java.util.List;

/**
 * Created by burke.li on 11/24/2015.
 */
public class DialogCameraSetting extends DialogView implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View btn_ok;
    private EditText et_delay;
    private Context context;

    private RadioGroup rg_flash_group;
    private RadioButton rb_flash_close;
    private RadioButton rb_flash_open;
    private RadioButton rb_flash_auto;
    private RadioButton rb_flash_torch;

    private RadioGroup rg_focus_group;
    private RadioButton rb_focus_auto;
    private RadioButton rb_focus_macro;
    private RadioButton rb_focus_continuousVideo;
    private RadioButton rb_focus_continuousPicture;

    private RadioGroup rg_scene_group;
    private RadioButton rb_scene_auto;
    private RadioButton rb_scene_snow;
    private RadioButton rb_scene_night;
    private RadioButton rb_scene_party;
    private RadioButton rb_scene_beach;
    private RadioButton rb_scene_theatre;
    private RadioButton rb_scene_candlelight;
    private RadioButton rb_scene_sunset;
    private RadioButton rb_scene_fireworks;
    private RadioButton rb_scene_sports;

    private RadioGroup rg_colorEffect_group;
    private RadioButton rb_colorEffect_none;
    private RadioButton rb_colorEffect_mono;
    private RadioButton rb_colorEffect_negative;
    private RadioButton rb_colorEffect_solarize;
    private RadioButton rb_colorEffect_sepia;
    private RadioButton rb_colorEffect_posterize;
    private RadioButton rb_colorEffect_whiteboard;
    private RadioButton rb_colorEffect_blackboard;
    private RadioButton rb_colorEffect_aqua;

    private RadioGroup rg_duoCamera_group;
    private RadioButton rb_backCamera;
    private RadioButton rb_frontCamera;

    private Camera camera;

    public DialogCameraSetting(Context context, Camera camera) {
        super(context, R.layout.dialog_camera_setting);
        this.context = context;
        this.camera = camera;
    }

    @Override
    public void showDialog() {
        super.showBegin(true);
        btn_ok = super.dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        et_delay = (EditText) super.dialog.findViewById(R.id.et_delay);
        checkCameraFlashMode();
        checkCameraFocusMode();
        checkCameraSceneMode();
        checkCameraColorEffectMode();
        checkDuoCamera();
        super.showEnd();
    }

    public void setParameters(int delay, int flashlightMode, int focusMode, int sceneMode, int colorEffectMode, int cameraID){
        if(delay!=0){
            et_delay.setText(delay+"");
            et_delay.setSelection(et_delay.getText().toString().length());
        }

        if(flashlightMode == TypeCamera.FLASHLIGHT_CLOSE){
            rb_flash_close.setChecked(true);
        }else if(flashlightMode == TypeCamera.FLASHLIGHT_OPEN){
            rb_flash_open.setChecked(true);
        }else if(flashlightMode == TypeCamera.FLASHLIGHT_AUTO){
            rb_flash_auto.setChecked(true);
        }else if(flashlightMode == TypeCamera.FLASHLIGHT_TORCH){
            rb_flash_torch.setChecked(true);
        }else{
            rb_flash_close.setChecked(true);
        }

        if(focusMode == TypeCamera.FOCUS_AUTO){
            rb_focus_auto.setChecked(true);
        }else if(focusMode == TypeCamera.FOCUS_MACRO){
            rb_focus_macro.setChecked(true);
        }else if(focusMode == TypeCamera.FOCUS_CONTINUOUS_VIDEO){
            rb_focus_continuousVideo.setChecked(true);
        }else if(focusMode == TypeCamera.FOCUS_CONTINUOUS_PICTURE){
            rb_focus_continuousPicture.setChecked(true);
        }else{
            rb_focus_auto.setChecked(true);
        }

        if(sceneMode == TypeCamera.SCENE_AUTO){
            rb_scene_auto.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_SNOW){
            rb_scene_snow.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_NIGHT){
            rb_scene_night.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_PARTY){
            rb_scene_party.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_BEACH){
            rb_scene_beach.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_THEATRE){
            rb_scene_theatre.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_CANDLELIGHT){
            rb_scene_candlelight.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_SUNSET){
            rb_scene_sunset.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_FIREWORKS){
            rb_scene_fireworks.setChecked(true);
        }else if(sceneMode == TypeCamera.SCENE_SPORTS){
            rb_scene_sports.setChecked(true);
        }else{
            rb_scene_auto.setChecked(true);
        }

        if(colorEffectMode == TypeCamera.COLOR_EFFECT_NONE){
            rb_colorEffect_none.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_MONO){
            rb_colorEffect_mono.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_NEGATIVE){
            rb_colorEffect_negative.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_SOLARIZE){
            rb_colorEffect_solarize.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_SEPIA){
            rb_colorEffect_sepia.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_POSTERIZE){
            rb_colorEffect_posterize.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_WHITEBOARD){
            rb_colorEffect_whiteboard.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_BLACKBOARD){
            rb_colorEffect_blackboard.setChecked(true);
        }else if(colorEffectMode == TypeCamera.COLOR_EFFECT_AQUA){
            rb_colorEffect_aqua.setChecked(true);
        }else{
            rb_colorEffect_none.setChecked(true);
        }

        if(cameraID == 0){
            rb_backCamera.setChecked(true);
        }else if(cameraID == 1){
            rb_frontCamera.setChecked(true);
        }else{
            rb_backCamera.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_ok:
                doSetting();
                break;
        }
    }

    private void doSetting() {
        if(!et_delay.getText().toString().isEmpty()&&Integer.parseInt(et_delay.getText().toString())>30){
            ToastUtils.toastMessage(context, R.string.error_camera_delay);
            return;
        }
        if (et_delay.getText().toString().isEmpty()) {
            ((CustomCameraActivity) context).setting(0, getFlashMode(), getFocusMode(), getSceneMode(), getColorEffectMode(), getCameraID());
        } else {
            ((CustomCameraActivity) context).setting(Integer.parseInt(et_delay.getText().toString()), getFlashMode(), getFocusMode(), getSceneMode(), getColorEffectMode(), getCameraID());
        }
        super.closeDialog(false);
    }

    private int getFlashMode(){
        if(rb_flash_close.isChecked()){
            return TypeCamera.FLASHLIGHT_CLOSE;
        }else if(rb_flash_open.isChecked()){
            return TypeCamera.FLASHLIGHT_OPEN;
        }else if(rb_flash_auto.isChecked()){
            return TypeCamera.FLASHLIGHT_AUTO;
        }else if(rb_flash_torch.isChecked()){
            return TypeCamera.FLASHLIGHT_TORCH;
        }
        return TypeCamera.FLASHLIGHT_AUTO;
    }

    private int getFocusMode(){
        if(rb_focus_auto.isChecked()){
            return TypeCamera.FOCUS_AUTO;
        }else if(rb_focus_macro.isChecked()){
            return TypeCamera.FOCUS_MACRO;
        }else if(rb_focus_continuousVideo.isChecked()){
            return TypeCamera.FOCUS_CONTINUOUS_VIDEO;
        }else if(rb_focus_continuousPicture.isChecked()){
            return TypeCamera.FOCUS_CONTINUOUS_PICTURE;
        }
        return TypeCamera.FOCUS_AUTO;
    }

    private int getSceneMode(){
        if(rb_scene_auto.isChecked()){
            return TypeCamera.SCENE_AUTO;
        }else if(rb_scene_snow.isChecked()){
            return TypeCamera.SCENE_SNOW;
        }else if(rb_scene_night.isChecked()){
            return TypeCamera.SCENE_NIGHT;
        }else if(rb_scene_party.isChecked()){
            return TypeCamera.SCENE_PARTY;
        }else if(rb_scene_beach.isChecked()){
            return TypeCamera.SCENE_BEACH;
        }else if(rb_scene_theatre.isChecked()){
            return TypeCamera.SCENE_THEATRE;
        }else if(rb_scene_candlelight.isChecked()){
            return TypeCamera.SCENE_CANDLELIGHT;
        }else if(rb_scene_sunset.isChecked()){
            return TypeCamera.SCENE_SUNSET;
        }else if(rb_scene_fireworks.isChecked()){
            return TypeCamera.SCENE_FIREWORKS;
        }else if(rb_scene_sports.isChecked()){
            return TypeCamera.SCENE_SPORTS;
        }
        return TypeCamera.SCENE_AUTO;
    }

    private int getColorEffectMode(){
        if(rb_colorEffect_none.isChecked()){
            return TypeCamera.COLOR_EFFECT_NONE;
        }else if(rb_colorEffect_mono.isChecked()){
            return TypeCamera.COLOR_EFFECT_MONO;
        }else if(rb_colorEffect_negative.isChecked()){
            return TypeCamera.COLOR_EFFECT_NEGATIVE;
        }else if(rb_colorEffect_solarize.isChecked()){
            return TypeCamera.COLOR_EFFECT_SOLARIZE;
        }else if(rb_colorEffect_sepia.isChecked()){
            return TypeCamera.COLOR_EFFECT_SEPIA;
        }else if(rb_colorEffect_posterize.isChecked()){
            return TypeCamera.COLOR_EFFECT_POSTERIZE;
        }else if(rb_colorEffect_whiteboard.isChecked()){
            return TypeCamera.COLOR_EFFECT_WHITEBOARD;
        }else if(rb_colorEffect_blackboard.isChecked()){
            return TypeCamera.COLOR_EFFECT_BLACKBOARD;
        }else if(rb_colorEffect_aqua.isChecked()){
            return TypeCamera.COLOR_EFFECT_AQUA;
        }
        return TypeCamera.COLOR_EFFECT_NONE;
    }

    private int getCameraID(){
        if(rb_backCamera.isChecked()){
            return TypeCamera.CAMERA_ID_BACK;
        }else if(rb_frontCamera.isChecked()){
            return TypeCamera.CAMERA_ID_FRONT;
        }
        return TypeCamera.CAMERA_ID_BACK;
    }

    private void checkCameraFlashMode(){
        rg_flash_group = (RadioGroup) super.dialog.findViewById(R.id.rg_flashGroup);
        rg_flash_group.setOnCheckedChangeListener(this);
        rb_flash_close = (RadioButton) super.dialog.findViewById(R.id.rb_flashClose);
        rb_flash_open = (RadioButton) super.dialog.findViewById(R.id.rb_flashOpen);
        rb_flash_auto = (RadioButton) super.dialog.findViewById(R.id.rb_flashAuto);
        rb_flash_torch = (RadioButton) super.dialog.findViewById(R.id.rb_flashTorch);

        rb_flash_close.setVisibility(View.GONE);
        rb_flash_open.setVisibility(View.GONE);
        rb_flash_auto.setVisibility(View.GONE);
        rb_flash_torch.setVisibility(View.GONE);

        List<String> flashList = camera.getParameters().getSupportedFlashModes();
        for(Iterator<String> it=flashList.iterator();it.hasNext();){
            String mode = it.next();
            if(mode.equals(Camera.Parameters.FLASH_MODE_OFF)){
                rb_flash_close.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FLASH_MODE_ON)){
                rb_flash_open.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FLASH_MODE_AUTO)){
                rb_flash_auto.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FLASH_MODE_TORCH)){
                rb_flash_torch.setVisibility(View.VISIBLE);
            }else{
                ToastUtils.toastMessage(context, R.string.error_camera_flash_mode);
            }
        }
    }

    private void checkCameraFocusMode(){
        rg_focus_group = (RadioGroup) super.dialog.findViewById(R.id.rg_focusGroup);
        rg_focus_group.setOnCheckedChangeListener(this);
        rb_focus_auto = (RadioButton) super.dialog.findViewById(R.id.rb_focusAuto);
        rb_focus_macro = (RadioButton) super.dialog.findViewById(R.id.rb_focusMacro);
        rb_focus_continuousVideo = (RadioButton) super.dialog.findViewById(R.id.rb_focusContinuousVideo);
        rb_focus_continuousPicture = (RadioButton) super.dialog.findViewById(R.id.rb_focusContinuousPicture);

        rb_focus_auto.setVisibility(View.GONE);
        rb_focus_macro.setVisibility(View.GONE);
        rb_focus_continuousVideo.setVisibility(View.GONE);
        rb_focus_continuousPicture.setVisibility(View.GONE);

        List<String> focusList = camera.getParameters().getSupportedFocusModes();
        for(Iterator<String> it=focusList.iterator();it.hasNext();){
            String mode = it.next();
            if(mode.equals(Camera.Parameters.FOCUS_MODE_AUTO)){
                rb_focus_auto.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FOCUS_MODE_MACRO)){
                rb_focus_macro.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                rb_focus_continuousVideo.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                rb_focus_continuousPicture.setVisibility(View.VISIBLE);
            }else{
                ToastUtils.toastMessage(context, R.string.error_camera_focus_mode);
            }
        }
    }

    private void checkCameraSceneMode(){
        rg_scene_group = (RadioGroup) super.dialog.findViewById(R.id.rg_SceneGroup);
        rg_scene_group.setOnCheckedChangeListener(this);
        rb_scene_auto = (RadioButton) super.dialog.findViewById(R.id.rb_SceneAuto);
        rb_scene_snow = (RadioButton) super.dialog.findViewById(R.id.rb_SceneSnow);
        rb_scene_night = (RadioButton) super.dialog.findViewById(R.id.rb_SceneNight);
        rb_scene_party = (RadioButton) super.dialog.findViewById(R.id.rb_SceneParty);
        rb_scene_beach = (RadioButton) super.dialog.findViewById(R.id.rb_SceneBeach);
        rb_scene_theatre = (RadioButton) super.dialog.findViewById(R.id.rb_SceneTheatre);
        rb_scene_candlelight = (RadioButton) super.dialog.findViewById(R.id.rb_SceneCandlelight);
        rb_scene_sunset = (RadioButton) super.dialog.findViewById(R.id.rb_SceneSunset);
        rb_scene_fireworks = (RadioButton) super.dialog.findViewById(R.id.rb_SceneFireworks);
        rb_scene_sports = (RadioButton) super.dialog.findViewById(R.id.rb_SceneSports);

        rb_scene_auto.setVisibility(View.GONE);
        rb_scene_snow.setVisibility(View.GONE);
        rb_scene_night.setVisibility(View.GONE);
        rb_scene_party.setVisibility(View.GONE);
        rb_scene_beach.setVisibility(View.GONE);
        rb_scene_theatre.setVisibility(View.GONE);
        rb_scene_candlelight.setVisibility(View.GONE);
        rb_scene_sunset.setVisibility(View.GONE);
        rb_scene_fireworks.setVisibility(View.GONE);
        rb_scene_sports.setVisibility(View.GONE);

        List<String> sceneList = camera.getParameters().getSupportedSceneModes();
        for(Iterator<String> it=sceneList.iterator();it.hasNext();){
            String mode = it.next();
            if(mode.equals(Camera.Parameters.SCENE_MODE_AUTO)){
                rb_scene_auto.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_SNOW)){
                rb_scene_snow.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_NIGHT)){
                rb_scene_night.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_PARTY)){
                rb_scene_party.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_BEACH)){
                rb_scene_beach.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_THEATRE)){
                rb_scene_theatre.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_CANDLELIGHT)){
                rb_scene_candlelight.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_SUNSET)){
                rb_scene_sunset.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_FIREWORKS)){
                rb_scene_fireworks.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.SCENE_MODE_SPORTS)){
                rb_scene_sports.setVisibility(View.VISIBLE);
            }else{
                ToastUtils.toastMessage(context, R.string.error_camera_scene_mode);
            }
        }
    }

    private void checkCameraColorEffectMode(){
        rg_colorEffect_group = (RadioGroup) super.dialog.findViewById(R.id.rg_ColorEffectGroup);
        rg_colorEffect_group.setOnCheckedChangeListener(this);
        rb_colorEffect_none = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectNone);
        rb_colorEffect_mono = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectMono);
        rb_colorEffect_negative = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectNegative);
        rb_colorEffect_solarize = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectSolarize);
        rb_colorEffect_sepia = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectSepia);
        rb_colorEffect_posterize = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectPosterize);
        rb_colorEffect_whiteboard = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectWhiteboard);
        rb_colorEffect_blackboard = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectBlackboard);
        rb_colorEffect_aqua = (RadioButton) super.dialog.findViewById(R.id.rb_ColorEffectAqua);

        rb_colorEffect_none.setVisibility(View.GONE);
        rb_colorEffect_mono.setVisibility(View.GONE);
        rb_colorEffect_negative.setVisibility(View.GONE);
        rb_colorEffect_solarize.setVisibility(View.GONE);
        rb_colorEffect_sepia.setVisibility(View.GONE);
        rb_colorEffect_posterize.setVisibility(View.GONE);
        rb_colorEffect_whiteboard.setVisibility(View.GONE);
        rb_colorEffect_blackboard.setVisibility(View.GONE);
        rb_colorEffect_aqua.setVisibility(View.GONE);

        List<String> colorEffectList = camera.getParameters().getSupportedColorEffects();
        for(Iterator<String> it=colorEffectList.iterator();it.hasNext();){
            String mode = it.next();
            if(mode.equals(Camera.Parameters.EFFECT_NONE)){
                rb_colorEffect_none.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_MONO)){
                rb_colorEffect_mono.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_NEGATIVE)){
                rb_colorEffect_negative.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_SOLARIZE)){
                rb_colorEffect_solarize.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_SEPIA)){
                rb_colorEffect_sepia.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_POSTERIZE)){
                rb_colorEffect_posterize.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_WHITEBOARD)){
                rb_colorEffect_whiteboard.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_BLACKBOARD)){
                rb_colorEffect_blackboard.setVisibility(View.VISIBLE);
            }else if(mode.equals(Camera.Parameters.EFFECT_AQUA)){
                rb_colorEffect_aqua.setVisibility(View.VISIBLE);
            }else{
                ToastUtils.toastMessage(context, R.string.error_camera_color_effect_mode);
            }
        }
    }

    private void checkDuoCamera(){
        rg_duoCamera_group = (RadioGroup) super.dialog.findViewById(R.id.rg_DuoCameraGroup);
        rg_duoCamera_group.setOnCheckedChangeListener(this);
        rb_backCamera = (RadioButton) super.dialog.findViewById(R.id.rb_backCamera);
        rb_frontCamera = (RadioButton) super.dialog.findViewById(R.id.rb_frontCamera);

        rb_backCamera.setVisibility(View.GONE);
        rb_frontCamera.setVisibility(View.GONE);

        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                rb_backCamera.setVisibility(View.VISIBLE);
            }
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rb_frontCamera.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_flashGroup:
                if(checkedId == R.id.rb_flashAuto||checkedId == R.id.rb_flashOpen){
                    rb_focus_auto.setChecked(true);
                }
                break;
            case R.id.rg_focusGroup:
                if(checkedId == R.id.rb_focusContinuousPicture||checkedId == R.id.rb_focusContinuousVideo||checkedId == R.id.rb_focusMacro){
                    rb_flash_close.setChecked(true);
                }
                break;
            case R.id.rg_SceneGroup:
                break;
            case R.id.rg_ColorEffectGroup:
                break;
            case R.id.rg_DuoCameraGroup:
                break;
        }
    }
}