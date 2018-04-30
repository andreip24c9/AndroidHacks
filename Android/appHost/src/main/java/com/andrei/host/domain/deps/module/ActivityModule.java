package com.andrei.host.domain.deps.module;

import android.app.Activity;
import android.content.Context;

import com.andrei.host.domain.deps.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrei on 25/03/2018.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity.getApplicationContext();
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

}
