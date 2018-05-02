package com.andrei.host.presentation.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrei.host.R;
import com.andrei.host.domain.data.model.Car;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Andrei on 24/03/2018.
 */

public class CarDetailsActivity extends AppCompatActivity {

    public static final String CAR_EXTRAS = "car_extras";

    private Car mCar;
    private ImageView mCarImg;
    private TextView mCarMake;
    private TextView mCarModel;
    private TextView mCarPrice;
    private TextView mCarYear;
    private TextView mCarKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        mCarImg = (ImageView) findViewById(R.id.car_img);
        mCarMake = (TextView) findViewById(R.id.car_make);
        mCarModel = (TextView) findViewById(R.id.car_model);
        mCarPrice = (TextView) findViewById(R.id.car_price);
        mCarYear = (TextView) findViewById(R.id.car_year);
        mCarKm = (TextView) findViewById(R.id.car_km);

        setupToolbar();
        setupDataFromBundle();
        setupCar();
    }

    private void setupDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCar = bundle.getParcelable(CAR_EXTRAS);
        }
    }

    private void setupCar() {
        if (mCar != null) {
            loadCarImage(mCar.getPictureUrl(), mCarImg);
            mCarMake.setText(mCar.getMake());
            mCarModel.setText(mCar.getModel());
            mCarPrice.setText(mCar.getPrice());
            mCarYear.setText(mCar.getYear());
            mCarKm.setText(mCar.getKm());
        }
    }

    void loadCarImage(final String url, final ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_car_placeholder_error);
        } else {
            Picasso.get()
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.ic_car_placeholder)
                                    .error(R.drawable.ic_car_placeholder_error)
                                    .into(imageView);
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_down);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
