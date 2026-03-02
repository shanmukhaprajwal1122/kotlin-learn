package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChargerReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "ChargerReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received broadcast: ${intent.action}")

        // Use goAsync() so the coroutine can safely finish before Android kills the process
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {

                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        Log.d(TAG, "Charger connected")
                        // Stop alarm service if it's ringing
                        val stopIntent = Intent(context, AlarmService::class.java)
                        context.stopService(stopIntent)
                    }

                    Intent.ACTION_POWER_DISCONNECTED -> {
                        Log.d(TAG, "Charger disconnected")
                        handleChargerDisconnected(context)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in onReceive: ${e.message}", e)
            } finally {
                // Signal that async work is done
                pendingResult.finish()
            }
        }
    }

    private suspend fun handleChargerDisconnected(context: Context) {
        val prefsManager = PreferencesManager(context)
        val isEnabled = prefsManager.isAlarmEnabled.first()

        if (isEnabled) {
            Log.d(TAG, "Alarm is enabled, starting AlarmService")
            val serviceIntent = Intent(context, AlarmService::class.java)
            context.startForegroundService(serviceIntent)
        } else {
            Log.d(TAG, "Alarm is disabled, ignoring event")
        }
    }
}