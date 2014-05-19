package com.rivergillis.androidgames.framework.gl;

import android.util.Log;
/**
 * This helper class simply counts the FPS and outputs that value to the log every second.
 * @author jgillis
 */

public class FPSCounter {
	long startTime = System.nanoTime();
	public int frames = 0;
	
	public void logFrame() {
		frames++;
		if(System.nanoTime() - startTime >= 1000000000) {
			Log.d("FPSCounter", "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
	}
}
