package com.rivergillis.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Uses a surfaceview to perform continuous rendering in a separate thread
 * @author jgillis
 *
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;				// instance of an AndroidGame
    Bitmap framebuffer;				// artificial framebuffer via bitmap
    Thread renderThread = null;		// the rendering thread
    SurfaceHolder holder;			// the surfaceholder
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();	// store the parameters
    }

    // Makes sure that our thread interacts nicely with the activity life cycle
    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      

    public void run() {
        Rect dstRect = new Rect();	// create a new rectangle
        long startTime = System.nanoTime();	//gets the initial start time
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;	// gets the nanotime of the next frame, divide to get it in seconds
            startTime = System.nanoTime();	//gets the time for the next frame

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);			// call the update and present methods each frame
            
            Canvas canvas = holder.lockCanvas();				// lock the canvas
            canvas.getClipBounds(dstRect);						// get the clip bounds
            canvas.drawBitmap(framebuffer, null, dstRect, null);// draw the framebuffer to the canvas
            holder.unlockCanvasAndPost(canvas);					// unlock and update the surfaceholder
        }
    }

    // terminates the rendering/main loop thread and waits for it to die completely before returning
    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }        
}
