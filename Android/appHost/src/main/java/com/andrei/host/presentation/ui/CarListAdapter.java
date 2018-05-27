package com.andrei.host.presentation.ui;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrei.host.R;
import com.andrei.host.domain.data.model.Car;
import com.andrei.host.domain.data.prefrences.PreferencesHelper;
import com.andrei.host.domain.data.storage.StorageUtils;
import com.andrei.host.presentation.ui.callbacks.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Andrei on 25/03/2018.
 */

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarHolder> {

    private List<Car> mCarList;
    private OnItemClickListener mListener;
    private int mLayout;

    private Bitmap mFakeImage;


    CarListAdapter(List<Car> mCarList, OnItemClickListener listener, AssetManager assetManager) {
        this.mCarList = mCarList;
        this.mListener = listener;

        try {
            InputStream inputStream = assetManager.open(StorageUtils.ANAKIN_FILE);
            mFakeImage = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLayout(int layout) {
        this.mLayout = layout;
    }

    @Override
    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(mLayout == PreferencesHelper.CAR_LIST_LAYOUT ? R.layout.car_list_item : R.layout.car_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CarHolder holder, int position) {
        holder.bind(mCarList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if (mCarList != null) {
            return mCarList.size();
        }
        return 0;
    }

    class CarHolder extends RecyclerView.ViewHolder {
        private ImageView mThumbnail;
        private TextView mMake;
        private TextView mModel;
        private TextView mPrice;

        CarHolder(View itemView) {
            super(itemView);
            mThumbnail = (ImageView) itemView.findViewById(R.id.car_img);
            mMake = (TextView) itemView.findViewById(R.id.car_make);
            mModel = (TextView) itemView.findViewById(R.id.car_model);
            mPrice = (TextView) itemView.findViewById(R.id.car_price);
        }

        void bind(final Car car, final OnItemClickListener listener) {
            if (mCarList.indexOf(car) != 0) {
                loadCarImage(car.getPictureUrl(), mThumbnail);
            } else {
                loadFakeCarImage(mThumbnail);
            }
            mMake.setText(car.getMake());
            mModel.setText(car.getModel());
            mPrice.setText(car.getPrice());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(car);
                }
            });
        }

        void loadFakeCarImage(ImageView imageView) {
            if (mFakeImage != null) {
                imageView.setImageBitmap(mFakeImage);
            }
        }

        void loadCarImage(String url, ImageView imageView) {
            if (url == null) {
                url = "https://secure.pic.autoscout24.net/images-420x315/602/014/0340014602001.jpg?41bca897e106bd1c0e73cbd6b70de5b4";
            }
            if (TextUtils.isEmpty(url)) {
                imageView.setImageResource(R.drawable.ic_car_placeholder_error);
            } else {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_car_placeholder)
                        .error(R.drawable.ic_car_placeholder_error)
                        .into(imageView);
            }
        }
    }
}
