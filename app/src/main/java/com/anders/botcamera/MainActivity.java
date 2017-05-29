package com.anders.botcamera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button auto = (Button) findViewById(R.id.btnAuto);
        auto.setOnClickListener(this);

        Button manual = (Button) findViewById(R.id.btnManual);
        manual.setOnClickListener(this);

        Button connect = (Button) findViewById(R.id.btnConnect);
        connect.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAuto:
                startActivity(new Intent(MainActivity.this, ColorBlobActivity.class));
                break;

            case R.id.btnManual:
                startActivity(new Intent(MainActivity.this, BotControl.class));
                break;

            case R.id.btnConnect:
                break;

            default:
                break;
        }
    }
}
