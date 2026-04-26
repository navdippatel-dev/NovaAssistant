package com.nova.assistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val viewModel: AssistantViewModel by viewModels()

    private lateinit var btnMic: ImageButton
    private lateinit var tvStatus: TextView
    private lateinit var tvTranscript: TextView
    private lateinit var tvResponse: TextView
    private lateinit var glowRing: android.view.View

    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.CAMERA
    )

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.speakWelcome()
        } else {
            Toast.makeText(this,
                "Some permissions denied. Nova may not work fully.",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMic = findViewById(R.id.btnMic)
        tvStatus = findViewById(R.id.tvStatus)
        tvTranscript = findViewById(R.id.tvTranscript)
        tvResponse = findViewById(R.id.tvResponse)
        glowRing = findViewById(R.id.glowRing)

        checkAndRequestPermissions()
        setupObservers()

        btnMic.setOnClickListener {
            if (viewModel.listeningState.value ==
                AssistantViewModel.ListeningState.IDLE) {
                viewModel.startListening()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val notGranted = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) !=
                    PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isNotEmpty()) {
            permissionLauncher.launch(notGranted.toTypedArray())
        } else {
            viewModel.speakWelcome()
        }
    }

    private fun setupObservers() {
        viewModel.listeningState.observe(this) { state ->
            when (state) {
                AssistantViewModel.ListeningState.IDLE -> {
                    tvStatus.text = "Tap to speak"
                    btnMic.alpha = 1.0f
                    glowRing.alpha = 0.4f
                }
                AssistantViewModel.ListeningState.LISTENING -> {
                    tvStatus.text = "Listening... 🎤"
                    btnMic.alpha = 1.0f
                    glowRing.alpha = 1.0f
                }
                AssistantViewModel.ListeningState.PROCESSING -> {
                    tvStatus.text = "Processing... ⚡"
                    btnMic.alpha = 0.6f
                    glowRing.alpha = 0.6f
                }
                null -> {}
            }
        }

        viewModel.transcriptText.observe(this) { text ->
            tvTranscript.text = if (text.isNotEmpty()) "\"$text\"" else ""
        }

        viewModel.responseText.observe(this) { text ->
            tvResponse.text = text
        }
    }
}