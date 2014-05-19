package com.rivergillis.androidgames.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.rivergillis.androidgames.framework.Audio;
import com.rivergillis.androidgames.framework.Music;
import com.rivergillis.androidgames.framework.Sound;

/**
 * Implementing the Audio class, this allows us to create new AndroidMusic and AndroidSound classes
 * @author jgillis
 *
 */
public class AndroidAudio implements Audio {
    AssetManager assets;	// used for loading sound effects from asset files
    SoundPool soundPool;	// holds sounds

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);			// sets the volume control of the media stream
        this.assets = activity.getAssets();									// get an assetmanager instance from the activity
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);	// create soundpool, able to play 20 sound effects in parallel
    }

    // Create an AssetFileDescriptor to send to a new AndroidMusic, which will create a new MediaPlayer
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }
    
    // Loads a sound effect from an asset into the SoundPool and returns an AndroidSound instance with the sound's ID
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}