package com.example.gridphotohomework.base

abstract class BasePresenter<V: BaseContract.View>: BaseContract.Presenter<V> {
    protected var view: V? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
    }

    override fun unsubscribe() {
    }
}