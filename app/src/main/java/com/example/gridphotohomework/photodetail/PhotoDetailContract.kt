package com.example.gridphotohomework.photodetail

import com.example.gridphotohomework.base.BaseContract

interface PhotoDetailContract {
    interface View : BaseContract.View {
        fun setId(id: Int)

        fun setTitle(title: String)

        fun setImage(url: String)
    }

    interface Presenter: BaseContract.Presenter<View>
}