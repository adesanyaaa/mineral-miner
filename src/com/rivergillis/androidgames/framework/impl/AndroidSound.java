package com.rivergillis.androidgames.framework.impl;

import android.media.SoundPool;

import com.rivergillis.androidgames.framework.Sound;

/**
 * Implementation of the Sound interface, holds and plays sounds
 * @author jgillis
 *
 */
public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;

    // Get the SoundPool and Id of the sound to play back
    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    // Plays the sound using the SoundPool
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    // Unloads the sound from the SoundPool
    public void dispose() {
        soundPool.unload(soundId);
    }
}
