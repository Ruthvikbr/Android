package com.example.cameraxpoc.videocapture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import com.example.cameraxpoc.R
import kotlinx.android.synthetic.main.activity_video_preview.*

class VideoPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_preview)

        val bundle = intent.extras
        if (bundle != null) {
            val filePath = bundle.getString("file_path")
            if (filePath != null) {
                Log.v("Video path", "$filePath")
                showVideo(filePath)
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    private fun showVideo(filePath: String) {

        val mediaController = MediaController(this)

        videoView.apply {
            setVideoPath(filePath)
            setMediaController(mediaController)
            requestFocus()
        }.start()
        mediaController.show(0)

    }
}