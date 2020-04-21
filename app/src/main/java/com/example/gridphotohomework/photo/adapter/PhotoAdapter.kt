package com.example.gridphotohomework.photo.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.example.gridphotohomework.R
import com.example.gridphotohomework.model.PhotoModel
import com.example.gridphotohomework.photo.PhotosActivity
import com.example.gridphotohomework.utils.WindowUtil
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


class PhotoAdapter(private val photos: List<PhotoModel>, private val clickListener: OnClickListener) : RecyclerView.Adapter<PhotoAdapter.Holder>() {
    private val TAG = PhotoAdapter::class.java.simpleName

    val mBitmapCache: LruCache<Int, Bitmap>

    init {
        val maxSize = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxSize / 8
        mBitmapCache = object : LruCache<Int, Bitmap>(cacheSize) {
            override fun sizeOf(key: Int, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(photo: PhotoModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.module_recycle_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return this.photos.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val screenWidth = WindowUtil.getScreenWidth(holder.itemView.context)
        val itemWidth = screenWidth / PhotosActivity.COLUMN_NUM
        val params = holder.frameLayout.layoutParams
        params.width = itemWidth
        params.height = itemWidth

        val bitmap = mBitmapCache.get(photos[position].id)
        if (bitmap != null) {
            holder.thumbnail.setImageBitmap(bitmap)
        }
        else {
            loadThumbnail(holder.thumbnail, photos[position].id, photos[position].thumbnailUrl)
        }

        holder.thumbnail.setOnClickListener {
            clickListener.onItemClick(photos[position])
        }

        holder.id.text = photos[position].id.toString()
        holder.title.text = photos[position].title
    }

    private fun loadThumbnail(icon: ImageView, id: Int, url: String) {
        if (cancelLoadTask(icon, id)) {
            val loadAlbumArt = LoadThumbnailTask(icon, id)
            val drawable = AsyncDrawable(loadAlbumArt)
            icon.setImageDrawable(drawable)
            loadAlbumArt.execute(url)
        }
    }

    private fun cancelLoadTask(icon: ImageView, id: Int): Boolean {
        val loadAlbumArt: LoadThumbnailTask = getLoadTask(icon) as LoadThumbnailTask? ?: return true
        if (loadAlbumArt.id !== id) {
            loadAlbumArt.cancel(true)
            return true
        }
        return false
    }

    private fun getLoadTask(icon: ImageView): AsyncTask<*, *, *>? {
        var task: LoadThumbnailTask? = null
        val drawable = icon.drawable
        if (drawable is AsyncDrawable) {
            task = drawable.loadArtworkTask
        }
        return task
    }

    private class AsyncDrawable(task: LoadThumbnailTask) :BitmapDrawable() {
        var loadArtworkTaskWeakReference: WeakReference<LoadThumbnailTask> = WeakReference(task)
        val loadArtworkTask: LoadThumbnailTask?
            get() = loadArtworkTaskWeakReference.get()
    }

    inner class LoadThumbnailTask(imageView: ImageView, val id: Int): AsyncTask<String, Void, Bitmap>() {
        private val mThumbnail: WeakReference<ImageView> = WeakReference(imageView)

        override fun doInBackground(vararg urls: String?): Bitmap? {
            if(isCancelled){
                return null
            }
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

        override fun onPostExecute(bitmap: Bitmap?) {
            if(isCancelled || bitmap == null){
                return
            }

            if (mThumbnail.get() != null) {
                val imageView = mThumbnail.get()
                if (imageView!!.drawable is AsyncDrawable) {
                    val task = (imageView.drawable as AsyncDrawable).loadArtworkTask
                    if (task != null && task == this) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
            mBitmapCache.put(id, bitmap)
            super.onPostExecute(bitmap)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frameLayout = itemView.findViewById<FrameLayout>(R.id.fl_container)
        val thumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
        val id = itemView.findViewById<TextView>(R.id.tv_id)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
    }
}