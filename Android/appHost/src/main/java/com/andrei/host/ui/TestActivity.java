package com.andrei.host.ui;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrei.host.R;
import com.andrei.host.security.RootInstaller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Button btnRoot, btnAccess, btnSelectApk, btnOpenAccess;

    public static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvTitle = (TextView) findViewById(R.id.title);
        btnRoot = (Button) findViewById(R.id.btn_root);
        btnAccess = (Button) findViewById(R.id.btn_access);
        btnSelectApk = (Button) findViewById(R.id.btn_selectapk);
        btnOpenAccess = (Button) findViewById(R.id.btn_openaccess);

        btnRoot.setOnClickListener(this);
        btnAccess.setOnClickListener(this);
        btnSelectApk.setOnClickListener(this);
        btnOpenAccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRoot) {
            rootInstall();
        }
        if (v == btnAccess) {
            accessInstall();
        }
        if (v == btnSelectApk) {
            selectApk();
        }
        if (v == btnOpenAccess) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
    }

    private void accessInstall() {
        final String strPath = tvTitle.getText().toString();
        if (!checkAccess(getPackageName() + getString(R.string.accessibilityservice_id))) {
            Toast.makeText(this, "The app does NOT have access to Accessibility", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "The app has access to Accessibility", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(strPath)) {
            return;
        }
        Uri uri = Uri.fromFile(new File(strPath));
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(localIntent);
    }

    private void startApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("ru.androidtools.selfclicker");
        // Run from the right place without the history of the application
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(launchIntent);
    }

    protected boolean checkAccess() {
        String serviceId = getString(R.string.accessibilityservice_id);
        List<AccessibilityServiceInfo> serviceInfoList = ((AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE)).getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo id : serviceInfoList) {
            if (serviceId.equals(id.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAccess(String serviceId) {
        int accessibilityEnabled = 0;
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ");


            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(TAG, "Setting: " + settingValue);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    Log.d(TAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(serviceId)) {
                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }

            Log.d(TAG, "***END***");
        } else {
            Log.d(TAG, "***ACCESSIBILIY IS DISABLED***");
        }
        return accessibilityFound;
    }

    private static final int FILE_SELECT = 0;

    private void rootInstall() {
        final String strPath = tvTitle.getText().toString();
        if (TextUtils.isEmpty(strPath)) {
            return;
        }
//        boolean isRoot = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
//        if (!isRoot) {
//            Toast.makeText(this, "No Root", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // TODO: 25/04/2018 remove this, tried to execute on main thread -- does not get su permission
        RootInstaller installer = new RootInstaller();
        final boolean result = installer.install(strPath);

        new Thread() {
            @Override
            public void run() {
                RootInstaller installer = new RootInstaller();
                final boolean result = installer.install(strPath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            Toast.makeText(getApplicationContext(), "Success install of malware", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed install of malware", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }.start();
    }

    private void selectApk() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose an APK"), FILE_SELECT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't find file browser", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                String path = getPath(this, uri);
                tvTitle.setText(path);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}