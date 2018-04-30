package com.andrei.host.presentation.ui.generics;

/**
 * Created by Andrei on 25/03/2018.
 */

public interface MvpPresenter<V extends MvpView> {

    /**
     * Attaches the view to the presenter. The view is dependent on a Activity lifecycle.
     * @param view The Mvp View to be attached
     */
    void attachView(V view);

    /**
     * Detaches the view from the presenter
     */
    void detachView();

    V getView();
    boolean isAttached();

}