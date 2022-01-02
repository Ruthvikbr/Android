package com.example.cameraxpoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cameraxpoc.imageanalysis.ImageAnalysisActivity
import com.example.cameraxpoc.imagecapture.ImageCaptureActivity
import com.example.cameraxpoc.videocapture.VideoCaptureActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraBtn: Button = findViewById(R.id.camera)
        val videoBtn: Button = findViewById(R.id.video)
        val imageAnalysisBtn: Button = findViewById(R.id.imageAnalysis)

        cameraBtn.setOnClickListener {
            startActivity(Intent(this, ImageCaptureActivity::class.java ))
        }
        videoBtn.setOnClickListener {
            startActivity(Intent(this, VideoCaptureActivity::class.java ))
        }
        imageAnalysisBtn.setOnClickListener {
            startActivity(Intent(this, ImageAnalysisActivity::class.java ))
        }
    }
}