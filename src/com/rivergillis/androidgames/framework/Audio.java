package com.rivergillis.androidgames.framework;

/**
 * This interface is a way to create new music and sound instances
 * @author jgillis
 *
 */
public interface Audio {
    public Music newMusic(String filename);

    public Sound newSound(String filename);
}
