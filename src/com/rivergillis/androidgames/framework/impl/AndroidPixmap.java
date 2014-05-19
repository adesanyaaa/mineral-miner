package com.rivergillis.androidgames.framework.impl;

import android.graphics.Bitmap;

import com.rivergillis.androidgames.framework.Pixmap;
import com.rivergillis.androidgames.framework.Graphics.PixmapFormat;

/**
 * This class stores a pixmapformat and a bitmap
 * @author jgillis
 *
 */
public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public PixmapFormat getFormat() {
        return format;
    }

    public void dispose() {
        bitmap.recycle();
    }      
}
