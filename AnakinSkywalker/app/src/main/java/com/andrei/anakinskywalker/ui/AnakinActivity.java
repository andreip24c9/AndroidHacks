package com.andrei.anakinskywalker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrei.anakinskywalker.R;
import com.andrei.anakinskywalker.security.Cryptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AnakinActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AnakinActivity.class.getSimpleName();

    public static final String KEY = "encryptionKeyAES";
    public static final String ANAKIN_FILE = "anakin.png";
    public static final String VADER_FILE = "vader.apk";
    public static final int EXTERNAL_STORAGE = 1;
    public Uri mContentUri;
    private ImageView mImgAnakin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "onCreate: ");
            requestPermissions(permissions, EXTERNAL_STORAGE);
        }

        mImgAnakin = (ImageView) findViewById(R.id.anakin_img);
        Button btn = (Button) findViewById(R.id.install_btn);

        setImgAnakin();
        btn.setOnClickListener(this);
    }

    public void setImgAnakin() {
        try {
            mImgAnakin.setImageBitmap(getBitmapFromAssets(ANAKIN_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE) {
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
                byte[] apk = decrypt(KEY, ANAKIN_FILE);
                saveExternalStorage(apk);
                installApk();
                break;

            default:
                break;
        }
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(mContentUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private byte[] decrypt(String key, String file) {
        try {
            byte[] iv = new byte[]{'f', 'M', (byte) 0xa3, 'T', (byte) 0xa1, (byte) 0xb0, 'j', '2', '|', (byte) 0xbb, (byte) 0xbf, '-', 'h', '6', (byte) 0xac, (byte) 0xe9};
            byte[] png = readBytes(file);

            byte[] decryptedAPK = Cryptography.decypherApk(key, iv, png);
            Log.d("decrypted_size", String.valueOf(decryptedAPK.length));
            Log.d(TAG, "photo decrypted");
            return decryptedAPK;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] readBytes(String file) throws IOException {
        InputStream is = getAssets().open(file);
        int size = is.available();
        byte[] assets = new byte[size];
        is.read(assets);
        is.close();
        Log.d(TAG, "readBytes: bytes read");
        return assets;
    }

    private void saveExternalStorage(byte[] bytes) {
        File filePath = new File(getFilesDir(), "hacks");
        File apkFile = new File(android.os.Environment.getExternalStorageDirectory().getPath(), VADER_FILE);
        mContentUri = Uri.fromFile(apkFile);
//        mContentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", apkFile);
        Log.d(TAG, "saveExternalStorage: " + mContentUri);

//        if(!apkFile.exists()) {
//            try {
//                apkFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(apkFile);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
