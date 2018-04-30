package com.andrei.host.presentation.application;

import android.app.Application;

import com.andrei.host.domain.deps.component.ApplicationComponent;
import com.andrei.host.domain.deps.component.DaggerApplicationComponent;
import com.andrei.host.domain.deps.module.ApplicationModule;
import com.andrei.host.domain.deps.module.NetworkModule;

/**
 * Created by Andrei on 25/03/2018.
 */

public class MyApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .networkModule(new NetworkModule())
                    .build();
            mApplicationComponent.inject(this);
        }
        return mApplicationComponent;
    }
}
