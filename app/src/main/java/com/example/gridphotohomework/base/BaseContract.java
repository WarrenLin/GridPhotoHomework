package com.example.gridphotohomework.base;

import android.app.Activity;

/**
 * Created by Warren on 2018/3/19.
 */

public interface BaseContract {

    interface View {
        Activity getActivity();
    }

    interface Presenter<V extends View>{
        void attachView(V view);

        void detachView();

        void subscribe();

        void unsubscribe();
    }
}
