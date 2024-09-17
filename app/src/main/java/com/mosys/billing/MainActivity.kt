package com.mosys.billing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.widget.TextView
import kotlinx.coroutines.*
import java.util.Base64
import android.util.Log

class MainActivity : Activity() {
    private lateinit var timerText: TextView
    private var job: Job? = null
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        timerText = findViewById(R.id.timerText)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TimerApp::WakelockTag")

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val seconds = intent.getIntExtra("seconds", 0)
        val customTextEncoded = intent.getStringExtra("customText") ?: ""
        val customText = decodeBase64(customTextEncoded)
        Log.d("TimerApp", "Received customText: $customText")

        if (seconds > 0) {
            startTimer(seconds, customText)
        }
    }

    private fun decodeBase64(encodedString: String): String {
        return try {
            val decodedBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getDecoder().decode(encodedString)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            String(decodedBytes, Charsets.UTF_8)
        } catch (e: IllegalArgumentException) {
            Log.e("TimerApp", "Error decoding Base64 string", e)
            "Error decoding message"
        }
    }

    private fun startTimer(seconds: Int, customText: String) {
        job?.cancel()
        wakeLock.acquire(seconds * 1000L + 5000L)

        job = CoroutineScope(Dispatchers.Main).launch {
            for (i in seconds downTo 0) {
                timerText.text = formatTime(i)
                delay(1000)
            }
            timerText.text = customText
            delay(5000)
            wakeLock.release()
        }
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}