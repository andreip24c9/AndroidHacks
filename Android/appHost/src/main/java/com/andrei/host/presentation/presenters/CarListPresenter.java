package com.andrei.host.presentation.presenters;

import com.andrei.host.domain.data.DataManager;
import com.andrei.host.domain.data.model.Car;
import com.andrei.host.presentation.ui.CarListView;
import com.andrei.host.presentation.ui.generics.MvpBasePresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrei on 25/03/2018.
 */

public class CarListPresenter extends MvpBasePresenter<CarListView> {

    private final DataManager mDataManager;

    public CarListPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void loadCars() {
        Call<List<Car>> cars = mDataManager.getCarList();
        cars.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful()) {
                    List<Car> cars = response.body();
                    onSuccess(cars);
                } else {
                    onFailed("Server Error");
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                onFailed("Server Error");
            }
        });
    }

    public void loadLayout() {
        if (isAttached()) {
            int layout = mDataManager.getCarListLayout();
            getView().onLoadLayout(layout);
        }
    }

    public void changeLayout(int layout) {
        mDataManager.setCarListLayout(layout);
        onLayoutChanged(layout);
    }

    private void onLayoutChanged(int layout) {
        if (isAttached()) {
            getView().onLayoutChanged(layout);
        }
    }

    private void onSuccess(List<Car> cars) {
        if (isAttached()) {
            // TODO: 26/03/2018 remove this
            cars.get(0).setPictureUrl("https://preview.c9users.io/carparked/backend_assets/carparked/4k_wallpaper.jpg?_c9_id=livepreview0&_c9_host=https://ide.c9.io");
            cars.get(1).setPictureUrl(null);
            getView().onLoadCars(cars);
        }
    }

    private void onFailed(String message) {
        if (isAttached()) {
            getView().onErrorLoadingCars(message);
        }
    }
}
