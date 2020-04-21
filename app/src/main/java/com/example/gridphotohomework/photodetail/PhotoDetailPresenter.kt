package com.example.gridphotohomework.photodetail

import com.example.gridphotohomework.base.BasePresenter

class PhotoDetailPresenter(private val urlString: String, val id: Int, private val title: String) :BasePresenter<PhotoDetailContract.View>(), PhotoDetailContract.Presenter {

    override fun subscribe() {
        view?.run {
            setId(id)
            setTitle(title)
            setImage(urlString)


        }
    }
}