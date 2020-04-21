package com.example.gridphotohomework.interactor

import android.util.Log
import com.example.gridphotohomework.model.PhotoModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL


class InteractorImpl : PhotoInteractor {
    val TAG = InteractorImpl::class.java.simpleName

    companion object {
        const val CONNECT_TIMEOUT = 10000  //10 seconds
        const val READ_TIMEOUT = 10000     //10 seconds
    }

    override fun getPhotos(): List<PhotoModel> {
        val apiUrl = "https://jsonplaceholder.typicode.com/photos"
        var connection: HttpURLConnection? = null
        val response = arrayListOf<PhotoModel>()
        try {
            val url = URL(apiUrl)
            connection = url.openConnection() as HttpURLConnection?
            connection?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                if (responseCode == 200) {
                    val responseText = inputStream.bufferedReader().use(BufferedReader::readText)
                    val jsonArray = JSONArray(responseText)
                    for(i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.get(i) as JSONObject
                        val albumId = jsonObject.optInt("albumId")
                        val id = jsonObject.optInt("id")
                        val title = jsonObject.optString("title")
                        val url = jsonObject.optString("url")
                        val thumbnailUrl = jsonObject.optString("thumbnailUrl")
                        val photoModel = PhotoModel(albumId = albumId,
                            id = id,
                            title = title,
                            url = url,
                            thumbnailUrl = thumbnailUrl)
                        response.add(photoModel)
                    }
                }
            }
        }
        catch (ex: Exception) {
            Log.e(TAG, ex.toString())
        }
        finally {
            connection?.disconnect()
        }
        return response
    }
}