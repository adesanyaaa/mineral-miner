package com.rivergillis.androidgames.framework;

import com.rivergillis.androidgames.framework.Graphics.PixmapFormat;

/**
 * Defines methods to help us work with a map of pixels
 * @author jgillis
 *
 */

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
