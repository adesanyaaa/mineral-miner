package com.rivergillis.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.rivergillis.androidgames.framework.Graphics;
import com.rivergillis.androidgames.framework.Pixmap;

/**
 * This class can draw pixels, lines, rectangles, and pixmaps to the framebuffer
 * @author jgillis
 *
 */
public class AndroidGraphics implements Graphics {
    AssetManager assets;		// used to load Bitmap instances
    Bitmap frameBuffer;			// a bitmap that represents the artificial framebuffer
    Canvas canvas;				// used to draw to the artificial framebuffer
    Paint paint;				// needed for drawing
    Rect srcRect = new Rect();	// Two Rect members needed for implementing the drawPixmap() methods
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;			// get an assetmanager and bitmap for the framebuffer
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();		// create a canvas and paint
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;				// create a null config
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;		// set the config depending on the pixmapformat
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();	// create a new Options
        options.inPreferredConfig = config;	// set the preferred config to the created config

        InputStream in = null;
        Bitmap bitmap = null;				// create istream and bitmap
        try {
            in = assets.open(fileName);		// open the filename
            bitmap = BitmapFactory.decodeStream(in);	// decode the istream into a bitmap
            if (bitmap == null)				// error if no bitmap
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();				// try to close the istream
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;	// set the format based on the bitmap's config
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);	// return a pixmap using the bitmap and format
    }

    // Extracts the rgb components of the specified 32-bit ARGB color parameter, and uses drawRGB to clear the framebuffer with that color, ignores alpha
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    // Draws a pixel of the framebuffer via the drawPoint method, setting the color first
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    // Sets the color and draws a line of the framebuffer
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    // sets the color and style of the pain
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);	// have to subtract 1 from the width and height
    }

    // Draws a portion of the pixmap
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;	// sets the properties of the source rectangle

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;		// sets the properties of the destination rectangle

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);	// Draws it, casting to an AndroidPixmap
    }

    // Draws a complete pixmap
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    // get the width and height of the framebuffer
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }
}

