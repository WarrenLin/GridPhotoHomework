package com.example.gridphotohomework.photo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gridphotohomework.R
import com.example.gridphotohomework.interactor.InteractorImpl
import com.example.gridphotohomework.model.PhotoModel
import com.example.gridphotohomework.photo.adapter.GridVerticalSpacingItemDecoration
import com.example.gridphotohomework.photo.adapter.PhotoAdapter
import kotlinx.android.synthetic.main.activity_photos.*

class PhotosActivity : AppCompatActivity(), PhotosContract.View {
    private val TAG = PhotosActivity::class.java.simpleName

    private var adapter :PhotoAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null

    private val presenter: PhotosPresenter by lazy { PhotosPresenter(InteractorImpl()) }

    companion object {
        const val COLUMN_NUM = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        presenter.attachView(this)
        presenter.subscribe()

        btn_retry.run {
            setOnClickListener {
                presenter.retry()
            }
        }
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun setData(photos: List<PhotoModel>?) {
        Log.d(TAG, "setData.")
        if (photos != null && photos.isNotEmpty()) {
            gridLayoutManager = GridLayoutManager(this, COLUMN_NUM)
            recycle_view.layoutManager = gridLayoutManager
            adapter = PhotoAdapter(photos, onPhotoClickListener)
            recycle_view.adapter = adapter
            val gridVerticalSpacingItemDecoration = GridVerticalSpacingItemDecoration(10, COLUMN_NUM)
            recycle_view.addItemDecoration(gridVerticalSpacingItemDecoration)
        }
        else {
            showErrorView()
        }
    }

    private val onPhotoClickListener = object: PhotoAdapter.OnClickListener {
        override fun onItemClick(photo: PhotoModel) {
            Log.d(TAG, "onItemClick:${photo.toString()}")
            presenter.onPhotoClick(photo)
        }
    }

    override fun showErrorView() {
        recycle_view.visibility = View.GONE
        ll_error.visibility = View.VISIBLE
    }

    override fun hideErrorView() {
        recycle_view.visibility = View.VISIBLE
        ll_error.visibility = View.GONE
    }

    override fun showErrorMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
