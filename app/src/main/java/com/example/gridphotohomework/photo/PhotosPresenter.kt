package com.example.gridphotohomework.photo

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.example.gridphotohomework.base.BasePresenter
import com.example.gridphotohomework.interactor.PhotoInteractor
import com.example.gridphotohomework.model.PhotoModel
import com.example.gridphotohomework.photodetail.PhotoDetailActivity
import com.example.gridphotohomework.utils.NetworkUtil
import java.lang.ref.WeakReference

class PhotosPresenter(private val photoInteractor: PhotoInteractor) : BasePresenter<PhotosContract.View>(), PhotosContract.Presenter {
    val TAG = PhotosPresenter::class.java.simpleName

    private var photoAsyncTask: PhotoAsyncTask? = null

    override fun subscribe() {
        Log.d(TAG, "subscribe")
        view?.let {
            if (NetworkUtil.isNetworkAvailable(it.activity)) {
                it.hideErrorView()
                it.showLoading()
                photoAsyncTask = PhotoAsyncTask(it, photoInteractor)
                photoAsyncTask!!.execute()
            }
            else {
                it.showErrorView()
                it.showErrorMsg("請檢察網路是否可用")
            }
        }
    }

    override fun retry() {
        Log.d(TAG, "retry")
        subscribe()
    }

    override fun onPhotoClick(photo: PhotoModel) {
        Log.d(TAG, "onPhotoClick: ${photo.toString()}")
        view?.let {
            val intent = Intent(it.activity, PhotoDetailActivity::class.java)
            intent.putExtra("url", photo.url)
            intent.putExtra("id", photo.id)
            intent.putExtra("title", photo.title)
            it.activity.startActivity(intent)
        }
    }

    companion object {
        class  PhotoAsyncTask(private val view: PhotosContract.View, private val photoInteractor: PhotoInteractor): AsyncTask<Void, Void, List<PhotoModel>>() {
            private val weakReference: WeakReference<PhotosContract.View> = WeakReference(view)

            override fun doInBackground(vararg p0: Void?): List<PhotoModel> {
                return photoInteractor.getPhotos()
            }

            override fun onPostExecute(result: List<PhotoModel>?) {
                super.onPostExecute(result)
                weakReference.get()?.run {
                    dismissLoading()
                    setData(result)
                }
            }
        }
    }

    override fun unsubscribe() {
        photoAsyncTask?.cancel(true)
    }
}