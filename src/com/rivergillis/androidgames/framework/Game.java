package com.rivergillis.androidgames.framework;

/**
 * This interface ties our other interfaces together, using it as a base
 * @author jgillis
 *
 */
public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}