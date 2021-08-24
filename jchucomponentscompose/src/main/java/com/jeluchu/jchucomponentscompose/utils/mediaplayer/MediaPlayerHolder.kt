package com.jeluchu.jchucomponentscompose.utils.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.jeluchu.jchucomponentscompose.core.extensions.ints.milliSecondsToTimer
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 *
 * Author: @Jeluchu
 *
 * This class is used to play music with MediaPlayer
 *
 * Once the [MediaPlayer] is released, it can't be used again, and another one has to be
 * created. In the onStop() method of the Activity the [MediaPlayer] is
 * released. Then in the onStart() of the Activity a new [MediaPlayer]
 * object has to be created. That's why this method is private, and called by load(int) and
 * not the constructor.
 *
 * References [PlayerAdapter] and [PlaybackInfoListener]
 *
 */


class MediaPlayerHolder(context: Context) : PlayerAdapter {
    private val mContext: Context = context.applicationContext
    private var mMediaPlayer: MediaPlayer? = null
    private var mResourceId = String.empty()
    private var mPlaybackInfoListener: PlaybackInfoListener? = null
    private var mExecutor: ScheduledExecutorService? = null
    private var mSeekbarPositionUpdateTask: Runnable? = null

    private fun initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setOnCompletionListener {
                stopUpdatingCallbackWithPosition()
                if (mPlaybackInfoListener != null) {
                    mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.State.COMPLETED)
                    mPlaybackInfoListener!!.onPlaybackCompleted()
                }
            }
        }
    }

    override val isPlaying: Boolean
        get() = if (mMediaPlayer != null) mMediaPlayer!!.isPlaying else false

    override val currentProgress: Float
        get() = if (mMediaPlayer != null) {
            val currentSeconds: Long = (mMediaPlayer!!.currentPosition / 1000).toLong()
            val totalSeconds: Long = (mMediaPlayer!!.duration / 1000).toLong()
            (currentSeconds.toDouble() / totalSeconds * 100).toFloat()
        } else 0F

    override val currentTime: String
        get() = if (mMediaPlayer != null) mMediaPlayer!!.currentPosition.milliSecondsToTimer()
        else String.empty()

    override val totalTime: String
        get() = if (mMediaPlayer != null) mMediaPlayer!!.duration.milliSecondsToTimer()
        else String.empty()

    override fun togglePlaying(isPlaying: Boolean) {
        if (mMediaPlayer != null) {
            when (isPlaying) {
                true -> mMediaPlayer!!.pause()
                false -> mMediaPlayer!!.start()
            }
        }
    }

    fun setPlaybackInfoListener(listener: PlaybackInfoListener?) {
        mPlaybackInfoListener = listener
    }

    override fun loadMedia(mp3Link: String) {
        mResourceId = mp3Link
        initializeMediaPlayer()
        try {
            mMediaPlayer!!.setDataSource(mContext, Uri.parse(mResourceId))
        } catch (e: Exception) {
            e.message
        }
        try {
            mMediaPlayer!!.prepare()
        } catch (e: Exception) {
            e.message
        }
        initializeProgressCallback()
    }

    override fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }


    override fun play() {
        if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.State.PLAYING)
            }
            startUpdatingCallbackWithPosition()
        }
    }

    override fun reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            loadMedia(mResourceId)
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.State.RESET)
            }
            stopUpdatingCallbackWithPosition()
        }
    }

    override fun pause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.State.PAUSED)
            }
        }
    }

    override fun seekTo(position: Int) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(position)
        }
    }

    private fun startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor()
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = Runnable { updateProgressCallbackTask() }
        }
        mExecutor!!.scheduleAtFixedRate(
            mSeekbarPositionUpdateTask,
            0,
            PLAYBACK_POSITION_REFRESH_INTERVAL_MS.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopUpdatingCallbackWithPosition() {
        if (mExecutor != null) {
            mExecutor!!.shutdownNow()
            mExecutor = null
            mSeekbarPositionUpdateTask = null
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onPositionChanged(0)
            }
        }
    }

    private fun updateProgressCallbackTask() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            val currentPosition = mMediaPlayer!!.currentPosition
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onPositionChanged(currentPosition)
            }
        }
    }

    override fun initializeProgressCallback() {
        val duration = mMediaPlayer!!.duration
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener!!.onDurationChanged(duration)
            mPlaybackInfoListener!!.onPositionChanged(0)
        }
    }

    companion object {
        const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000
    }

}