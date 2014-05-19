package com.rivergillis.androidgames.framework;

/**
 * Defines methods to help use play a short sound clip that is kept in memory
 * @author jgillis
 *
 */

public interface Sound {
    public void play(float volume);

    public void dispose();
}
