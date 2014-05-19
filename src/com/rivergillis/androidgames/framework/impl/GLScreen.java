package com.rivergillis.androidgames.framework.impl;

import com.rivergillis.androidgames.framework.Game;
import com.rivergillis.androidgames.framework.Screen;

/**
 * Helper class, stores a reference to GLGraphics
 * @author jgillis
 *
 */
public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = glGame.getGLGraphics();
    }
}
