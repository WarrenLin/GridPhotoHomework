package com.example.gridphotohomework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gridphotohomework.photo.PhotosActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.run {
            setOnClickListener {
                Log.d(TAG, "OnClick Start")
                startActivity(Intent(this@MainActivity, PhotosActivity::class.java))
            }
        }
    }
}
