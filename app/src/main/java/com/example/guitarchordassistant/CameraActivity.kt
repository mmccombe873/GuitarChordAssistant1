package com.example.guitarchordassistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.guitarchordassistant.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide action bar before showing UI
        supportActionBar?.hide()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listeners for take photo and video capture buttons
        val toggle: ToggleButton = binding.toggleButton
        binding.imageCaptureButton.setOnClickListener{ takePhoto() }
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                binding.imageCaptureButton.setBackgroundResource(R.drawable.videorecordicon)
                binding.imageCaptureButton.setOnClickListener{ captureVideo() }
            } else {
                // The toggle is disabled
                binding.imageCaptureButton.setBackgroundResource(R.drawable.shutter2)
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        val extras: Bundle? = intent.extras
        if(extras != null) {
            val imageId = extras.get("curImage").toString()
            val image = resources.getIdentifier(imageId, "drawable", packageName)
            binding.chordOverlay.setImageResource(image)
        }

        binding.chordLibrary.setOnClickListener {
            val intent = Intent(applicationContext, ChordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and file in internal storage
        val filename = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
        val file = File(applicationContext.filesDir, filename)

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )

    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        binding.imageCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }

        // create and start a new recording session
        val filename = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + ".mp4"
        val file = File(applicationContext.filesDir, filename)

        val outputOptions = FileOutputOptions
            .Builder(file)
            .build()
        recording = videoCapture.output
            .prepareRecording(this, outputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(this@CameraActivity,
                        Manifest.permission.RECORD_AUDIO) ==
                    PermissionChecker.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when(recordEvent) {
                    is VideoRecordEvent.Start -> {
                        binding.imageCaptureButton.apply {
                            setBackgroundResource(R.drawable.stoprecord)
                            isEnabled = true
                        }
                        val msg = "Recording started"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, msg)
                        binding.toggleButton.visibility = View.INVISIBLE
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "Video capture succeeded: " +
                                    "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, msg)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(
                                TAG, "Video capture ends with error: " +
                                        "${recordEvent.error}")
                        }
                        binding.imageCaptureButton.apply {
                            setBackgroundResource(R.drawable.videorecordicon)
                            isEnabled = true
                        }
                        binding.toggleButton.visibility = View.VISIBLE

                    }
                }
            }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Recorder
            val recorder = Recorder.Builder()
                .setQualitySelector(
                    QualitySelector.from(
                        Quality.HIGHEST,
                    // select supported quality if the required Quality.HIGHEST
                    // is not supportable with the imageCapture use case.
                    FallbackStrategy.higherQualityOrLowerThan(Quality.SD)))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // Image Capture creation
            imageCapture = ImageCapture.Builder().build()

            // Select front camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture,
                )

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "GuitAR Chord Assistant"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hideSystemUI()
        Log.d(TAG, "onSaveInstanceState Called")
    }

}