package com.rivergillis.androidgames.framework;

/**
 * Defines methods the help us play a streamed audio file
 * @author jgillis
 *
 */

public interface Music {
    public void play();

    public void stop();

    public void pause();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();
}
