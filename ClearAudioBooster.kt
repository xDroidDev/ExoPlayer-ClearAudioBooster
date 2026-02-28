package com.xdroiddev.xdplayer.utils

import android.media.audiofx.DynamicsProcessing
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener

/**
 * ClearAudioBooster: A professional-grade software volume amplifier for ExoPlayer.
 * Uses DynamicsProcessing (Limiter) to provide up to 200% volume with zero cracking/clipping.
 *
 * Architected by: xDroidDev
 */
@OptIn(UnstableApi::class)
class ClearAudioBooster(private val player: ExoPlayer) {

    private var dynamicsProcessing: DynamicsProcessing? = null
    
    // We use AnalyticsListener to catch the AudioSessionId as soon as the media starts
    private val listener = object : AnalyticsListener {
        override fun onAudioSessionIdChanged(
            eventTime: AnalyticsListener.EventTime,
            audioSessionId: Int
        ) {
            initializeLimiter(audioSessionId)
        }
    }

    init {
        player.addAnalyticsListener(listener)
    }

    private fun initializeLimiter(audioSessionId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                dynamicsProcessing?.release()

                // Configuration: 1ms Attack, 60ms Release, 10:1 Ratio, -2dB Threshold
                val limiter = DynamicsProcessing.Limiter(
                    true, true, 0, 1f, 60f, 10f, -2f, 0f
                )

                val builder = DynamicsProcessing.Config.Builder(
                    DynamicsProcessing.VARIANT_FAVOR_FREQUENCY_RESOLUTION,
                    2, false, 0, false, 0, false, 0, true
                )
                builder.setLimiterByChannelIndex(0, limiter)
                builder.setLimiterByChannelIndex(1, limiter)

                dynamicsProcessing = DynamicsProcessing(0, audioSessionId, builder.build()).apply {
                    enabled = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Boosts volume from 100% (1.0f) to 200% (2.0f).
     * The Limiter prevents audio distortion by compressing peaks.
     */
    fun setBoostLevel(level: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val boostDb = if (level > 1f) (level - 1f) * 15f else 0f
            try {
                dynamicsProcessing?.let { dp ->
                    val limiter = dp.getLimiterByChannelIndex(0)
                    if (limiter.postGain != boostDb) {
                        limiter.postGain = boostDb
                        dp.setLimiterAllChannelsTo(limiter)
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun release() {
        player.removeAnalyticsListener(listener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                dynamicsProcessing?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dynamicsProcessing = null
        }
    }
}
