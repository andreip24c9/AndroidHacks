package com.andrei.host.domain.data.storage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Andrei on 20/03/2018.
 */

public class StorageUtils {

    public static final String TAG = StorageUtils.class.getSimpleName();

    public static final String ANAKIN_FILE = "anakin.png";
    public static final String VADER_FILE = "vader.apk";
    public static Uri mContentUri;

    public static void saveExternalStorage(byte[] bytes, Context context) {
        File filePath = new File(context.getFilesDir(), "hacks");
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
