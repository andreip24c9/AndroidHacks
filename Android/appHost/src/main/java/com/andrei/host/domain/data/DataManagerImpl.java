package com.andrei.host.domain.data;

import com.andrei.host.domain.data.model.Car;
import com.andrei.host.domain.data.networking.NetworkApi;
import com.andrei.host.domain.data.prefrences.PreferencesHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Created by Andrei on 25/03/2018.
 */

@Singleton
public class DataManagerImpl implements DataManager {

    private final NetworkApi mNetworkApi;
    private final PreferencesHelper mPrefHelper;

    @Inject
    DataManagerImpl(PreferencesHelper preferencesHelper, NetworkApi networkAPI) {
        this.mPrefHelper = preferencesHelper;
        this.mNetworkApi = networkAPI;
    }

    @Override
    public int getCarListLayout() {
        return mPrefHelper.getCarListLayout();
    }

    @Override
    public void setCarListLayout(int layout) {
        mPrefHelper.setCarListLayout(layout);
    }

    @Override
    public Call<List<Car>> getCarList() {
        return mNetworkApi.getCarList();
    }
}
