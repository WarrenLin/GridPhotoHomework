package com.example.gridphotohomework.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(private val imageView: ImageView, private val loading: ProgressBar) : AsyncTask<String, Void, Bitmap>() {
    override fun onPreExecute() {
        super.onPreExecute()
        imageView.setImageDrawable(null)
        loading.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg urls: String?): Bitmap? {
        var bmp: Bitmap? = null
        var connection: HttpURLConnection? = null
        urls[0]?.let {
            try {
                val url = URL(it)
                connection = url.openConnection() as HttpURLConnection?
                connection?.run {
                    setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
                    )
                    doInput = true
                    connect()
                    if (responseCode == 200) {
                        val inputStream: InputStream = this.inputStream
                        bmp = BitmapFactory.decodeStream(inputStream)
                    }
                    else {
                        Log.e("LoadImageTask", "responseCode err:$responseCode , $it")
                    }
                }
            } catch (e: Exception) {
                Log.e("LoadImageTask", e.toString())
                e.printStackTrace()
            }
            finally {
                connection?.disconnect()
            }
        }
        return bmp
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        loading.visibility = View.GONE
        result?.let {
            this.imageView.setImageBitmap(result)
        } ?: kotlin.run {
            this.imageView.setImageResource(android.R.drawable.ic_dialog_alert)
        }
    }
}