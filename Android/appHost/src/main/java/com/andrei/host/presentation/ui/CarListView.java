package com.andrei.host.presentation.ui;

import com.andrei.host.domain.data.model.Car;
import com.andrei.host.presentation.ui.generics.MvpView;

import java.util.List;

/**
 * Created by Andrei on 25/03/2018.
 */

public interface CarListView extends MvpView {
    void onLoadCars(List<Car> carList);
    void onErrorLoadingCars(String message);

    void onLoadLayout(int layout);
    void onLayoutChanged(int layout);
}
