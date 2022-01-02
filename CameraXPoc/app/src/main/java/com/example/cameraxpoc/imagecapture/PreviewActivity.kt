package com.example.cameraxpoc.imagecapture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.cameraxpoc.R
import java.io.File

class PreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val imageView: ImageView = findViewById(R.id.photoImage)
        val bundle = intent.extras

        if (bundle != null) {
            val filePath = bundle.getString("file_path")
            val file = File(filePath)
            Glide
                .with(this)
                .load(file)
                .into(imageView)
        } else {
            finish()
        }
    }
}