package com.example.gridphotohomework.photo

import com.example.gridphotohomework.base.BaseContract
import com.example.gridphotohomework.model.PhotoModel

interface PhotosContract {
    interface View : BaseContract.View {
        fun showLoading()

        fun dismissLoading()

        fun setData(photos: List<PhotoModel>?)

        fun showErrorView()

        fun hideErrorView()

        fun showErrorMsg(msg: String)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun retry()

        fun onPhotoClick(photo: PhotoModel)
    }
}