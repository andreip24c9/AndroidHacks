package com.andrei.host.domain.data.prefrences;

/**
 * Created by Andrei on 25/03/2018.
 */

public interface PreferencesHelper {

    int CAR_LIST_LAYOUT = 0;
    int CAR_GRID_LAYOUT = 1;

    int getCarListLayout();
    void setCarListLayout(int layout);

}
