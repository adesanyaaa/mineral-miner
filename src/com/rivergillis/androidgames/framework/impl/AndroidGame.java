package com.rivergillis.androidgames.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.rivergillis.androidgames.framework.Audio;
import com.rivergillis.androidgames.framework.FileIO;
import com.rivergillis.androidgames.framework.Game;
import com.rivergillis.androidgames.framework.Graphics;
import com.rivergillis.androidgames.framework.Input;
import com.rivergillis.androidgames.framework.Screen;

/**
 * This class ties all of the implementations together,
 * It also performs window management, managing wakelocks, and
 * managing screens and integrating them into the activity life cycle
 * 
 * It is also an activity
 * @author jgillis
 *
 */
public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;	// we'll draw to this, and it will manage our main loop thread
    Graphics graphics;
    Audio audio;
    Input input;						// these members will be set the Android* implementations
    FileIO fileIO;
    Screen screen;						// holds currently active screen
    WakeLock wakeLock;					// holds wakelock that we use to keep the screen from dimming

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	// make the activity fullscreen
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
         * Set up the artificial framebuffer depending on the devices orientation
         * Portrait will result in a 320x480 frame
         * Landscape will result in a 480x320
         */
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        
        // Calculate the scaleX and scaleY for use with the touch handlers to make a fixed-coordinate system
        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();

        // Instantiate a bunch of classes
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);	// Communicates touches with the AndroidFastRenderView
        screen = getStartScreen();		// Games will implement this method
        setContentView(renderView);		// set the AndroidFastRenderView as the content view of the Activity
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);	// grab a powermanager
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");			// grab a wakelock from the powermanager
    }

    // Acquires a wakelock, informs screen that the game & activity is resumed, resumed AndroidFastRenderView to resume the rendering thread
    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    // Releases the wakelock, terminates rendering thread, pause the screen.
    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    // Returns the respective instances to the caller
    public Input getInput() {
        return input;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");	// null checking

        this.screen.pause();		// pause and dispose the current screen to make room for next screen
        this.screen.dispose();
        screen.resume();			// resumes the new screen
        screen.update(0);			// updates the new screen once with a delta time of zero
        this.screen = screen;		// sets the new screen as the current screen
    }

    // returns the currently active screen
    public Screen getCurrentScreen() {
        return screen;
    }
}
