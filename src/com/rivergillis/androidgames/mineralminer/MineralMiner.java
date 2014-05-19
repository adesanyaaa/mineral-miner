package com.rivergillis.androidgames.mineralminer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.rivergillis.androidgames.framework.Screen;
import com.rivergillis.androidgames.framework.impl.GLGame;

/**
 * The main entry point of the app, performs a little initial set-up
 * After that, it takes the user to a main miner screen
 * @author jgillis
 *
 */
public class MineralMiner extends GLGame {
	boolean firstTimeCreate = true;
	
	public Screen getStartScreen() {
		return new MinerScreen(this);	// returns a new miner screen instance as a start screen
	}

    // Called each time the GLES context is re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);			// load assets and settings if first time creating
            firstTimeCreate = false;            
        } else {
            Assets.reload();			// if not first time, reload the assets
        }
    }     
    
//    // Also pause the music if it is playing
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(Settings.soundEnabled)
//            Assets.music.pause();
//    }
}
