package com.andrei.host.presentation.ui.generics;

import java.lang.ref.WeakReference;

/**
 * Created by Andrei on 25/03/2018.
 */

public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> mView;

    @Override
    public void attachView(V view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        mView.clear();
    }

    @Override
    public boolean isAttached() {
        return mView != null && mView.get() != null;
    }

    @Override
    public V getView() {
        if (isAttached())
            return mView.get();
        return null;
    }
}
