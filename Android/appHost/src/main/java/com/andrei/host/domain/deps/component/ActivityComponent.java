package com.andrei.host.domain.deps.component;

import com.andrei.host.domain.deps.ActivityScope;
import com.andrei.host.domain.deps.module.ActivityModule;
import com.andrei.host.domain.deps.module.NetworkModule;
import com.andrei.host.presentation.ui.CarListActivity;

import dagger.Component;

/**
 * Created by Andrei on 25/03/2018.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, NetworkModule.class})
public interface ActivityComponent {
    void inject(CarListActivity activity);
}

