package com.llf.mybitmapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.llf.mybitmapapp.util.BitmapUtils;
import com.llf.mybitmapapp.util.ToastUtils;

public class PictureProgressActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnProgress;
    private Button btnSynthesis;
    private View includeProgress;
    private ImageView imgPicture;
    private Button btnChoosePicture;
    private Button btnZoomUp;
    private Button btnZoomDown;
    private Button btnTurnLeft;
    private Button btnTurnRight;
    private Button btnLightUp;
    private Button btnLightDown;
    private Button btnContrastUp;
    private Button btnContrastDown;
    private Button btnSaturationUp;
    private Button btnSaturationDown;
    private Button btnMirrorX;
    private Button btnMirrorY;
    private View includeSynthesis;
    private ImageView imgPicture1;
    private ImageView imgPicture2;
    private ImageView imgResultPicture;
    private Button btnChoosePicture1;
    private Button btnChoosePicture2;
    private Button btnSynthesisLighten;
    private Button btnSynthesisDarken;
    private Button btnSynthesisMultiply;
    private Button btnSynthesisScreen;

    private int imageWidth;
    private int imageHeight;

    private static final int WHICH_PROGRESS_PICTURE = 0;
    private static final int WHICH_SYNTHESIS_PICTURE1 = 1;
    private static final int WHICH_SYNTHESIS_PICTURE2 = 2;
    private static final int MIRROR_X = 0;
    private static final int MIRROR_Y = 1;
    private static final int SYNTHESIS_LIGHTEN = 0;
    private static final int SYNTHESIS_DARKEN = 1;
    private static final int SYNTHESIS_MULTIPLY = 2;
    private static final int SYNTHESIS_SCREEN = 3;
    private int which = WHICH_PROGRESS_PICTURE;

    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Bitmap bitmapCanvas;
    private Canvas canvasSynthesis;
    private Paint paintSynthesis;
    private Bitmap bitmap1, bitmap2;
    private Bitmap bitmapSynthesisCanvas;
    private float zoomNum = 1f;
    private float rotateNum = 0f;
    private float lightNum = 0f;
    private float contrastNum = 1f;
    private float saturationNum = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_progress);

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        imageWidth = displaysMetrics.widthPixels / 3;
        imageHeight = displaysMetrics.heightPixels / 3;
        Log.i("MMMM", "imageWidth = " + imageWidth);
        Log.i("MMMM", "imageHeight = " + imageHeight);
        initView();
    }

    private void initView() {
        btnProgress = (Button) findViewById(R.id.btn_panelProgress);
        btnSynthesis = (Button) findViewById(R.id.btn_panelSynthesis);
        btnProgress.setOnClickListener(this);
        btnSynthesis.setOnClickListener(this);

        includeProgress = findViewById(R.id.include_pictureProgress);
        includeProgress.setVisibility(View.GONE);
        imgPicture = (ImageView) findViewById(R.id.img_Picture);
        btnChoosePicture = (Button) findViewById(R.id.btn_choosePicture);
        btnZoomUp = (Button) findViewById(R.id.btn_zoomUp);
        btnZoomDown = (Button) findViewById(R.id.btn_zoomDown);
        btnTurnLeft = (Button) findViewById(R.id.btn_turnLeft);
        btnTurnRight = (Button) findViewById(R.id.btn_turnRight);
        btnLightUp = (Button) findViewById(R.id.btn_lightUp);
        btnLightDown = (Button) findViewById(R.id.btn_lightDown);
        btnContrastUp = (Button) findViewById(R.id.btn_contrastUp);
        btnContrastDown = (Button) findViewById(R.id.btn_contrastDown);
        btnSaturationUp = (Button) findViewById(R.id.btn_saturationUp);
        btnSaturationDown = (Button) findViewById(R.id.btn_saturationDown);
        btnMirrorX = (Button) findViewById(R.id.btn_mirrorX);
        btnMirrorY = (Button) findViewById(R.id.btn_mirrorY);
        btnChoosePicture.setOnClickListener(this);
        btnZoomUp.setOnClickListener(this);
        btnZoomDown.setOnClickListener(this);
        btnTurnLeft.setOnClickListener(this);
        btnTurnRight.setOnClickListener(this);
        btnLightUp.setOnClickListener(this);
        btnLightDown.setOnClickListener(this);
        btnContrastUp.setOnClickListener(this);
        btnContrastDown.setOnClickListener(this);
        btnSaturationUp.setOnClickListener(this);
        btnSaturationDown.setOnClickListener(this);
        btnMirrorX.setOnClickListener(this);
        btnMirrorY.setOnClickListener(this);

        includeSynthesis = findViewById(R.id.include_pictureSynthesis);
        includeSynthesis.setVisibility(View.GONE);
        imgPicture1 = (ImageView) findViewById(R.id.img_picture1);
        imgPicture2 = (ImageView) findViewById(R.id.img_picture2);
        imgResultPicture = (ImageView) findViewById(R.id.img_resultPicture);
        btnChoosePicture1 = (Button) findViewById(R.id.btn_choosePicture1);
        btnChoosePicture2 = (Button) findViewById(R.id.btn_choosePicture2);
        btnSynthesisLighten = (Button) findViewById(R.id.btn_synthesisLighten);
        btnSynthesisDarken = (Button) findViewById(R.id.btn_synthesisDarken);
        btnSynthesisMultiply = (Button) findViewById(R.id.btn_synthesisMultiply);
        btnSynthesisScreen = (Button) findViewById(R.id.btn_synthesisScreen);
        btnChoosePicture1.setOnClickListener(this);
        btnChoosePicture2.setOnClickListener(this);
        btnSynthesisLighten.setOnClickListener(this);
        btnSynthesisDarken.setOnClickListener(this);
        btnSynthesisMultiply.setOnClickListener(this);
        btnSynthesisScreen.setOnClickListener(this);

        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) imgPicture1.getLayoutParams();
        rlParams.width = imageWidth;
        rlParams.height = imageHeight;
        imgPicture1.setLayoutParams(rlParams);
        rlParams = (RelativeLayout.LayoutParams) imgPicture2.getLayoutParams();
        rlParams.width = imageWidth;
        rlParams.height = imageHeight;
        imgPicture2.setLayoutParams(rlParams);
        rlParams = (RelativeLayout.LayoutParams) imgResultPicture.getLayoutParams();
        rlParams.width = imageWidth;
        rlParams.height = imageHeight;
        imgResultPicture.setLayoutParams(rlParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Uri imageUri = data.getData();
                if (which == WHICH_PROGRESS_PICTURE) {
                    bitmap = BitmapUtils.compressBitmap(this, imageUri, imageWidth, imageHeight);
                    double maxLength = Math.sqrt(bitmap.getWidth() * bitmap.getWidth() + bitmap.getHeight() * bitmap.getHeight());
                    bitmapCanvas = Bitmap.createBitmap((int) maxLength, (int) maxLength, bitmap.getConfig());
                    canvas = new Canvas(bitmapCanvas);
                    paint = new Paint();
                    canvas.drawBitmap(bitmap, (int) (maxLength - bitmap.getWidth()) / 2, (int) (maxLength - bitmap.getHeight()) / 2, paint);
                    zoomNum = 1f;
                    rotateNum = 0f;
                    lightNum = 0f;
                    saturationNum = 1f;
                    contrastNum = 1f;
                    imgPicture.setImageBitmap(bitmapCanvas);
                } else if (which == WHICH_SYNTHESIS_PICTURE1) {
                    bitmap1 = BitmapUtils.loadImage(this, imageUri, imageWidth, imageHeight);
                    imgPicture1.setImageBitmap(bitmap1);

                } else if (which == WHICH_SYNTHESIS_PICTURE2) {
                    bitmap2 = BitmapUtils.loadImage(this, imageUri, imageWidth, imageHeight);
                    imgPicture2.setImageBitmap(bitmap2);
                } else {
                    ToastUtils.toastMessage(this, R.string.error_pick_picture);
                }
                break;
            case RESULT_CANCELED:
                ToastUtils.toastMessage(this, R.string.cancel);
                break;
        }
    }

    private void doMatrix(Bitmap bitmap, float zoomNum, float rotateNum, float lightNum, float contrastNum) {
//        bitmapCanvas = Bitmap.createBitmap((int)(bitmap.getWidth()*zoomNum), (int)(bitmap.getHeight()*zoomNum), bitmap.getConfig());
        saturationNum = 1f;
        double maxLength = Math.sqrt((bitmap.getWidth() * zoomNum) * (bitmap.getWidth() * zoomNum) + (bitmap.getHeight() * zoomNum) * (bitmap.getHeight() * zoomNum));
        bitmapCanvas = Bitmap.createBitmap((int) maxLength, (int) maxLength, bitmap.getConfig());
        canvas = new Canvas(bitmapCanvas);
        Matrix matrix = new Matrix();
        matrix.setScale(zoomNum, zoomNum);
        matrix.postTranslate((int) (maxLength - bitmap.getWidth() * zoomNum) / 2, (int) (maxLength - bitmap.getHeight() * zoomNum) / 2);
        matrix.postRotate(rotateNum, bitmapCanvas.getWidth() / 2, bitmapCanvas.getHeight() / 2);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{contrastNum, 0, 0, 0, lightNum,
                0, contrastNum, 0, 0, lightNum,
                0, 0, contrastNum, 0, lightNum,
                0, 0, 0, 1, 0,});
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private void doMirror(Bitmap bitmap, float zoomNum, float rotateNum, float lightNum, float contrastNum, int mirrorType) {
        double maxLength = Math.sqrt((bitmap.getWidth() * zoomNum) * (bitmap.getWidth() * zoomNum) + (bitmap.getHeight() * zoomNum) * (bitmap.getHeight() * zoomNum));
        bitmapCanvas = Bitmap.createBitmap((int) maxLength, (int) maxLength, bitmap.getConfig());
        canvas = new Canvas(bitmapCanvas);
        Matrix matrix = new Matrix();
        matrix.setScale(zoomNum, zoomNum);
        matrix.postTranslate((int) (maxLength - bitmap.getWidth() * zoomNum) / 2, (int) (maxLength - bitmap.getHeight() * zoomNum) / 2);
        matrix.postRotate(rotateNum, bitmapCanvas.getWidth() / 2, bitmapCanvas.getHeight() / 2);

        if (mirrorType == MIRROR_X) {
            matrix.postScale(-1, 1);
            matrix.postTranslate(bitmapCanvas.getWidth(), 0);
        } else if (mirrorType == MIRROR_Y) {
            matrix.postScale(1, -1);
            matrix.postTranslate(0, bitmapCanvas.getHeight());
        }

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{contrastNum, 0, 0, 0, lightNum,
                0, contrastNum, 0, 0, lightNum,
                0, 0, contrastNum, 0, lightNum,
                0, 0, 0, 1, 0,});
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private void doSaturation(float saturationNum) {
        lightNum = 0f;
        contrastNum = 1f;
        double maxLength = Math.sqrt((bitmap.getWidth() * zoomNum) * (bitmap.getWidth() * zoomNum) + (bitmap.getHeight() * zoomNum) * (bitmap.getHeight() * zoomNum));
        bitmapCanvas = Bitmap.createBitmap((int) maxLength, (int) maxLength, bitmap.getConfig());
        canvas = new Canvas(bitmapCanvas);
        Matrix matrix = new Matrix();
        matrix.setScale(zoomNum, zoomNum);
        matrix.postTranslate((int) (maxLength - bitmap.getWidth() * zoomNum) / 2, (int) (maxLength - bitmap.getHeight() * zoomNum) / 2);
        matrix.postRotate(rotateNum, bitmapCanvas.getWidth() / 2, bitmapCanvas.getHeight() / 2);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturationNum);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    private void doSynthesis(int synthesisType) {

        if (bitmap1 == null) {
            ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_pick_picture1);
            return;
        }
        if (bitmap2 == null) {
            ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_pick_picture2);
            return;
        }
        bitmapSynthesisCanvas = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        canvasSynthesis = new Canvas(bitmapSynthesisCanvas);
        paintSynthesis = new Paint();
        canvasSynthesis.drawBitmap(bitmap1, 0, 0, paintSynthesis);
        switch (synthesisType) {
            case SYNTHESIS_LIGHTEN:
                paintSynthesis.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
                break;
            case SYNTHESIS_DARKEN:
                paintSynthesis.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
                break;
            case SYNTHESIS_MULTIPLY:
                paintSynthesis.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                break;
            case SYNTHESIS_SCREEN:
                paintSynthesis.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                break;
        }
        canvasSynthesis.drawBitmap(bitmap2, 0, 0, paintSynthesis);
        imgResultPicture.setImageBitmap(bitmapSynthesisCanvas);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_panelProgress:
                includeProgress.setVisibility(View.VISIBLE);
                includeSynthesis.setVisibility(View.GONE);
                break;
            case R.id.btn_panelSynthesis:
                includeProgress.setVisibility(View.GONE);
                includeSynthesis.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_choosePicture:
                which = WHICH_PROGRESS_PICTURE;
                Intent intent_choose_pictrue = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_choose_pictrue, 0);
                break;
            case R.id.btn_zoomUp:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (zoomNum > 2.0f) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_zoom_up);
                    return;
                }
                zoomNum = zoomNum + 0.2f;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_zoomDown:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (zoomNum < 0.3f) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_zoom_down);
                    return;
                }
                zoomNum = zoomNum - 0.2f;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_turnLeft:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                rotateNum = rotateNum + 30;
                if (rotateNum == 360) {
                    rotateNum = 0;
                }
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_turnRight:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                rotateNum = rotateNum - 30;
                if (rotateNum == -360) {
                    rotateNum = 0;
                }
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_lightUp:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (lightNum >= 200) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_light_up);
                    return;
                }
                lightNum = lightNum + 50;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_lightDown:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (lightNum <= -200) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_light_down);
                    return;
                }
                lightNum = lightNum - 50;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_contrastUp:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (contrastNum >= 2) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_contrast_up);
                    return;
                }
                contrastNum = contrastNum + 0.2f;
                lightNum = -(contrastNum - 1) * 150;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_contrastDown:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (contrastNum < 0.1) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_contrast_down);
                    return;
                }
                contrastNum = contrastNum - 0.2f;
                lightNum = -(contrastNum - 1) * 150;
                doMatrix(bitmap, zoomNum, rotateNum, lightNum, contrastNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_saturationUp:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (saturationNum >= 3) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_contrast_down);
                    return;
                }
                saturationNum = saturationNum + 0.2f;
                doSaturation(saturationNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_saturationDown:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                if (saturationNum <= -1) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_contrast_down);
                    return;
                }
                saturationNum = saturationNum - 0.2f;
                doSaturation(saturationNum);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_mirrorX:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                doMirror(bitmap, zoomNum, rotateNum, lightNum, contrastNum, MIRROR_X);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_mirrorY:
                if (bitmap == null) {
                    ToastUtils.toastMessage(PictureProgressActivity.this, R.string.error_camera_null2);
                    return;
                }
                doMirror(bitmap, zoomNum, rotateNum, lightNum, contrastNum, MIRROR_Y);
                imgPicture.setImageBitmap(bitmapCanvas);
                break;
            case R.id.btn_choosePicture1:
                which = WHICH_SYNTHESIS_PICTURE1;
                Intent intent_choose_pictrue1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_choose_pictrue1, 0);
                break;
            case R.id.btn_choosePicture2:
                which = WHICH_SYNTHESIS_PICTURE2;
                Intent intent_choose_pictrue2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_choose_pictrue2, 0);
                break;
            case R.id.btn_synthesisLighten:
                doSynthesis(SYNTHESIS_LIGHTEN);
                break;
            case R.id.btn_synthesisDarken:
                doSynthesis(SYNTHESIS_DARKEN);
                break;
            case R.id.btn_synthesisMultiply:
                doSynthesis(SYNTHESIS_MULTIPLY);
                break;
            case R.id.btn_synthesisScreen:
                doSynthesis(SYNTHESIS_SCREEN);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (includeProgress.getVisibility() == View.VISIBLE || includeSynthesis.getVisibility() == View.VISIBLE) {
            includeProgress.setVisibility(View.GONE);
            includeSynthesis.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
