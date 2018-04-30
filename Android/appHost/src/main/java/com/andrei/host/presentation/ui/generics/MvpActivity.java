package com.andrei.host.presentation.ui.generics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.andrei.host.domain.deps.component.ActivityComponent;
import com.andrei.host.domain.deps.component.DaggerActivityComponent;
import com.andrei.host.presentation.application.MyApplication;

/**
 * Created by Andrei on 25/03/2018.
 */

public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpView {

    protected P mPresenter;
    private ActivityComponent mActivityComp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityComp = DaggerActivityComponent.builder()
                .applicationComponent(((MyApplication) getApplication()).getComponent())
                .build();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create a presenter for this activity
        mPresenter = createPresenter();

        // attach this activity to the presenter
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // detach from presenter, it should cancel the requests in order not to waist resources
        mPresenter.detachView();
    }

    /**
     * Adding a presenter to the view.
     *
     * @return An instance of the presenter that will handle the logic for the View.
     */
    @NonNull
    public abstract P createPresenter();

    public ActivityComponent getActivityComp() {
        return mActivityComp;
    }

}
