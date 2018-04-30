package com.andrei.host.domain.data.prefrences;

import android.content.Context;
import android.content.SharedPreferences;

import com.andrei.host.domain.deps.ApplicationContext;
import com.andrei.host.domain.deps.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Andrei on 25/03/2018.
 */

@Singleton
public class PreferencesImpl implements PreferencesHelper {

    private static final String KEY_LIST_LAYOUT = "cars_layout";

    private final SharedPreferences mSharedPrefs;

    @Inject
    PreferencesImpl(
            @ApplicationContext Context context,
            @PreferenceInfo String prefFileName) {
        mSharedPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public int getCarListLayout() {
        return mSharedPrefs.getInt(KEY_LIST_LAYOUT, CAR_GRID_LAYOUT);
    }

    @Override
    public void setCarListLayout(int layout) {
        mSharedPrefs.edit().putInt(KEY_LIST_LAYOUT, layout).apply();
    }
}
