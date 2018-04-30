package com.andrei.host.domain.deps.component;

import android.content.Context;

import com.andrei.host.domain.data.DataManager;
import com.andrei.host.domain.deps.ApplicationContext;
import com.andrei.host.domain.deps.module.ApplicationModule;
import com.andrei.host.domain.deps.module.NetworkModule;
import com.andrei.host.presentation.application.MyApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrei on 25/03/2018.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(MyApplication app);

    @ApplicationContext
    Context context();

    DataManager getDataManager();
}