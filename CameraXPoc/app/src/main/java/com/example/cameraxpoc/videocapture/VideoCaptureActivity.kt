package com.example.cameraxpoc.videocapture

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import com.example.cameraxpoc.R
import kotlinx.android.synthetic.main.activity_video_capture_acitivity.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VideoCaptureActivity : AppCompatActivity() {

    private lateinit var videoCapture: VideoCapture<Recorder>

    private lateinit var outputDirectory: File

    private var currentRecording: Recording? = null

    private lateinit var recordingState: VideoRecordEvent

    private val captureLiveStatus = MutableLiveData<String>()

    private val mainThreadExecutor by lazy { ContextCompat.getMainExecutor(this) }

    enum class UiState {
        IDLE,       // Not recording, all UI controls are active.
        RECORDING,  // Camera is recording, only display Pause/Resume & Stop button.
        FINALIZED // Recording just completes, disable all RECORDING UI controls.

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_capture_acitivity)

        if (checkPermissionsGranted()) {
            startPreview()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        outputDirectory = getOutputDirectory()

        setupListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissionsGranted()) {
                startPreview()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setupListeners() {

        captureLiveStatus.observe(this) {
            capture_status.apply {
                post { text = it }
            }
        }

        captureLiveStatus.postValue("")

        capture_button.setOnClickListener {
            if (!this@VideoCaptureActivity::recordingState.isInitialized || recordingState is VideoRecordEvent.Finalize) {
                startRecording()
            } else {
                when (recordingState) {
                    is VideoRecordEvent.Start -> {
                        currentRecording?.pause()
                        stop_button.visibility = View.VISIBLE
                    }
                    is VideoRecordEvent.Pause -> {
                        currentRecording?.resume()
                    }
                    is VideoRecordEvent.Resume -> {
                        currentRecording?.pause()
                    }
                }
            }
        }

        stop_button.setOnClickListener {
            stop_button.visibility = View.INVISIBLE
            if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
                return@setOnClickListener
            }
            val recording = currentRecording
            if (recording != null) {
                recording.stop()
                currentRecording = null
            }

            capture_button.setImageResource(R.drawable.ic_resume)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir !== null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun startPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .build()

            videoCapture = VideoCapture.withOutput(recorder)

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))


    }

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        val videoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".mp4"
        )

        val outputOptions = FileOutputOptions.Builder(videoFile)
            .build()

        currentRecording = videoCapture.output
            .prepareRecording(this, outputOptions)
            .apply { withAudioEnabled() }
            .start(mainThreadExecutor, captureListener)


    }

    private val captureListener = Consumer<VideoRecordEvent> { event ->
        if (event !is VideoRecordEvent.Status) {
            recordingState = event
        }

        updateUI(event)

        if (event is VideoRecordEvent.Finalize) {
            galleryAddVideo(System.currentTimeMillis().toString())
            val intent = Intent(this@VideoCaptureActivity, VideoPreviewActivity::class.java)
            val bundle = Bundle().apply {
                putString("file_path", event.outputResults.outputUri.toString())
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun updateUI(event: VideoRecordEvent) {
        val state = if (event is VideoRecordEvent.Status) recordingState.getNameString()
        else event.getNameString()
        when (event) {
            is VideoRecordEvent.Status -> {
                // placeholder: we update the UI with new status after this when() block,
                // nothing needs to do here.
            }
            is VideoRecordEvent.Start -> {
                showUI(UiState.RECORDING, event.getNameString())
            }
            is VideoRecordEvent.Finalize -> {
                showUI(UiState.FINALIZED, event.getNameString())
            }
            is VideoRecordEvent.Pause -> {
                capture_button.setImageResource(R.drawable.ic_resume)
            }
            is VideoRecordEvent.Resume -> {
                capture_button.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
            }
        }

        val stats = event.recordingStats
        val size = stats.numBytesRecorded / 1000
        val time = java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(stats.recordedDurationNanos)
        var text = "${state}: recorded ${size}KB, in ${time}second"
        if (event is VideoRecordEvent.Finalize) {
            text = ""
        }

        capture_status.text = text


    }

    private fun VideoRecordEvent.getNameString(): String {
        return when (this) {
            is VideoRecordEvent.Status -> "Status"
            is VideoRecordEvent.Start -> "Started"
            is VideoRecordEvent.Finalize -> "Finalized"
            is VideoRecordEvent.Pause -> "Paused"
            is VideoRecordEvent.Resume -> "Resumed"
            else -> throw IllegalArgumentException("Unknown VideoRecordEvent: $this")
        }
    }

    private fun checkPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showUI(state: UiState, status: String = "") {
        when (state) {
            UiState.IDLE -> {
                capture_button.setImageResource(R.drawable.ic_resume)
                stop_button.visibility = View.INVISIBLE
            }
            UiState.RECORDING -> {
                capture_button.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                capture_button.isEnabled = true
                stop_button.visibility = View.VISIBLE
                stop_button.isEnabled = true
            }
            UiState.FINALIZED -> {
                capture_button.setImageResource(R.drawable.ic_resume)
                stop_button.visibility = View.INVISIBLE
            }
        }
        capture_status.text = status

    }

    private fun galleryAddVideo(name: String) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }

            }
            contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)

        } catch (e: Exception) {
            Log.v("Exception", "${e.message}")
        }
    }

    companion object {
        private const val TAG = "CameraX - Video capture"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 11
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }
}