package com.example.islamiapp.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.islamiapp.R

class RadioService : Service() {

    private val binder = RadioBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var isMuted = false

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_URL = "EXTRA_URL"
        const val EXTRA_NAME = "EXTRA_NAME"
        private const val CHANNEL_ID = "radio_channel"
        private const val NOTIFICATION_ID = 1
    }

    inner class RadioBinder : Binder() {
        fun getService(): RadioService = this@RadioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
                val name = intent.getStringExtra(EXTRA_NAME) ?: "Islamic Radio"
                startRadio(url, name)
            }

            ACTION_STOP -> stopRadio()
        }
        return START_STICKY
    }

    fun startRadio(url: String, stationName: String) {
        stopRadio()
        isMuted = false

        startForeground(NOTIFICATION_ID, buildNotification(stationName))

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { it.start() }
            setOnErrorListener { _, _, _ ->
                stopSelf()
                true
            }
        }
    }

    fun stopRadio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isMuted = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun mute() {
        mediaPlayer?.setVolume(0f, 0f)
        isMuted = true
    }

    fun unmute() {
        mediaPlayer?.setVolume(1f, 1f)
        isMuted = false
    }

    fun toggleMute(): Boolean {
        return if (isMuted) {
            unmute()
            false
        } else {
            mute()
            true
        }
    }

    fun isMuted(): Boolean = isMuted
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    private fun buildNotification(stationName: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Islamic Radio")
            .setContentText(stationName)
            .setSmallIcon(R.drawable.ic_play)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Islamic Radio",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Plays Islamic radio in the background"
            }
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
