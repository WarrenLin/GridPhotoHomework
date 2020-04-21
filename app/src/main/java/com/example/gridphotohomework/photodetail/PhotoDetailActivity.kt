package com.example.gridphotohomework.photodetail

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gridphotohomework.R
import com.example.gridphotohomework.utils.ImageLoader
import com.example.gridphotohomework.utils.NetworkUtil
import kotlinx.android.synthetic.main.activity_photo_detail.*

class PhotoDetailActivity : AppCompatActivity(), PhotoDetailContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        val urlString = intent.getStringExtra("url")
        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")

        if (urlString == null || id == 0 || title == null) {
            return
        }
        val presenter = PhotoDetailPresenter(urlString, id, title)
        presenter.attachView(this)
        presenter.subscribe()

        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "請檢察網路是否可用", Toast.LENGTH_LONG).show()
        }
    }

    override fun setId(id: Int) {
        tv_id.text = id.toString()
    }

    override fun setTitle(title: String) {
        tv_title.text = title
    }

    override fun setImage(url: String) {
        ImageLoader(iv_photo, loading).execute(url)
    }

    override fun getActivity(): Activity {
        return this
    }
}
