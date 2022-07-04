/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.utils.mediaplayer

interface PlayerAdapter {

    fun loadMedia(mp3Link: String)
    fun release()

    val isPlaying: Boolean
    val currentProgress: Float
    val currentTime: String
    val totalTime: String

    fun play()
    fun reset()
    fun pause()
    fun stop()

    fun togglePlaying(isPlaying: Boolean)

    fun initializeProgressCallback()
    fun seekTo(position: Int)

}