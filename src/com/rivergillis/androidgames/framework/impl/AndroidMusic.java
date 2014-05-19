package com.rivergillis.androidgames.framework.impl;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.rivergillis.androidgames.framework.Music;

/**
 * Implementing the Music class, this class allows us to play back
 * streamed audio files using a mediaPlayer
 * @author jgillis
 *
 */
public class AndroidMusic implements Music, OnCompletionListener {
    MediaPlayer mediaPlayer;		// used to play music
    boolean isPrepared = false;		// used to keep track of the mediaPlayer's state

    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();	// create the MediaPlayer
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),	// Set the mediaPlayer to the asset
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();						// Prepare it and set the flag
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);	// inform us when it ends
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    // Checks if the mediaplayer is still going, if so, stops it
    public void dispose() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
    }
    
    // These next 3 methods simply tell us information about the mediaPlayer's current state
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped() {
        return !isPrepared;
    }
    
    // Pause the mediaPlayer if it is playing
    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }
    
    // Play the file with the media player
    public void play() {
        if (mediaPlayer.isPlaying())	// quit if the mediaPlayer is already playing
            return;
        try {
        	/*
        	 * we use a synchronized block since we are using isPrepared, which might get set on a separate
        	 * thread since we are using the OnCompletionListener
        	 */
            synchronized (this) {		
                if (!isPrepared)
                    mediaPlayer.prepare();	// prepare the mediaPlayer if it isn't and start it
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set whether the mediaPlayer is looping
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    // Set the volume
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }
    
    // Stops the mediaPlayer and sets the isPrepared flag in a synchronized block
    public void stop() {
        mediaPlayer.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    // Called when the mediaPlayer is done playing, sets the isPrepared flag in a synchronized block
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}
