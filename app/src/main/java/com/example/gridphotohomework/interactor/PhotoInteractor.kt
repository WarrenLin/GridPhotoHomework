package com.example.gridphotohomework.interactor

import com.example.gridphotohomework.model.PhotoModel

interface PhotoInteractor {
    fun getPhotos(): List<PhotoModel>
}