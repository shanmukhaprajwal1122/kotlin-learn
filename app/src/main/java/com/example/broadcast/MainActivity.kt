package com.example.broadcast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var prefsManager: PreferencesManager

    private lateinit var tvStatus: TextView
    private lateinit var tvAlarmName: TextView
    private lateinit var btnEnable: Button
    private lateinit var btnChange: Button

    private var isAlarmEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = PreferencesManager(this)

        // Initialize views
        tvStatus = findViewById(R.id.tvStatus)
        tvAlarmName = findViewById(R.id.tvAlarmName)
        btnEnable = findViewById(R.id.btnEnable)
        btnChange = findViewById(R.id.btnChange)

        // Setup observers
        setupObservers()

        // Setup button listeners
        setupButtons()
    }

    private fun setupObservers() {
        // Observe alarm enabled status
        lifecycleScope.launch {
            prefsManager.isAlarmEnabled.collect { enabled ->
                isAlarmEnabled = enabled
                updateUI()
            }
        }

        // Observe alarm name
        lifecycleScope.launch {
            prefsManager.alarmAudioName.collect { name ->
                tvAlarmName.text = "Current Alarm: $name"
            }
        }
    }

    private fun setupButtons() {
        btnEnable.setOnClickListener {
            lifecycleScope.launch {
                if (isAlarmEnabled) {
                    // Disable alarm and stop service if ringing
                    prefsManager.setAlarmEnabled(false)
                    stopService(Intent(this@MainActivity, AlarmService::class.java))
                } else {
                    // Enable alarm
                    prefsManager.setAlarmEnabled(true)
                }
            }
        }

        btnChange.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun updateUI() {
        if (isAlarmEnabled) {
            tvStatus.text = "Alarm Status: Enabled ✓"
            btnEnable.text = "Disable Alarm / Stop Ringing"
        } else {
            tvStatus.text = "Alarm Status: Disabled"
            btnEnable.text = "Enable Alarm"
        }
    }
}