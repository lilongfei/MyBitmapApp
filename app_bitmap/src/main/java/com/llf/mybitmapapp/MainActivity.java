package com.llf.mybitmapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private View btnDefaultCamera;
    private View btnCustomCamera;
    private View btnPictrueProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        initView();
    }

    private void initView() {
        btnDefaultCamera = findViewById(R.id.rl_defaultCamera);
        btnCustomCamera = findViewById(R.id.rl_customCamera);
        btnPictrueProgress = findViewById(R.id.rl_pictureProgress);
        btnDefaultCamera.setOnClickListener(this);
        btnCustomCamera.setOnClickListener(this);
        btnPictrueProgress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_defaultCamera:
                if (intent == null) {
                    intent = new Intent(MainActivity.this, CaptureBitmapActivity.class);
                }
                intent.putExtra("title", R.string.default_camera);
                startActivity(intent);
                break;
            case R.id.rl_customCamera:
                if (intent == null) {
                    intent = new Intent(MainActivity.this, CaptureBitmapActivity.class);
                }
                intent.putExtra("title", R.string.custom_camera);
                startActivity(intent);
                break;
            case R.id.rl_pictureProgress:
                Intent intent_progress = new Intent(MainActivity.this, PictureProgressActivity.class);
                startActivity(intent_progress);
                break;
        }
    }
}
