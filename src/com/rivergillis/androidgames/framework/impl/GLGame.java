package com.rivergillis.androidgames.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
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
 * This activity serves as an 'AndroidGame' class, but for OpenGL ES games
 * @author jgillis
 *
 */
public abstract class GLGame extends Activity implements Game, Renderer {
	
	// Keeps track of the state that the GLGame instance is in
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	// members
	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();	// used to synchronize the UI and rendering threads
	long startTime = System.nanoTime();
	WakeLock wakeLock;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	// make the activity fullscreen
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		glView = new GLSurfaceView(this);
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);			// required for new versions of android
		glView.setRenderer(this);
		setContentView(glView);			// create new GLSurfaceView, set it as the content view
		
		// Instantiate a bunch of classes
		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);		//do not scale the touches
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);		//used to create a wakelock instance
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	// let GLSurfaceView resume the rendering thread, acquire a wakelock
	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		
		// invoked on the rendering thread
		synchronized(stateChanged) {
			if(state == GLGameState.Initialized)
				screen = getStartScreen();		// starts the game if the state is initialized
			state = GLGameState.Running;		// changes state to running
			screen.resume();					// calls the current Screen's resume method
			startTime = System.nanoTime();		// keeps track of time
		}
	}
	
	// Stub
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	}
	
	// Called by the rendering thread as often as possible
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		
		synchronized(stateChanged) {
			state = this.state;			// get the current game state
		}
		
		if(state == GLGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f; //gets delta time in seconds
			startTime = System.nanoTime();
			
			screen.update(deltaTime);	// call the screen's update()
			screen.present(deltaTime);	// call the screen's present()
		}
		
		if(state == GLGameState.Paused) {
			screen.pause();					// pause the screen
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;	// change the state to idle
				stateChanged.notifyAll();		// notify the UI thread that it can now truly pause the application
			}
		}
		
		if(state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();				// Pause and dispose of the screen
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;	// change state to idle
				stateChanged.notifyAll();		// notify the UI thread
			}
		}
	}
	
	@Override
	public void onPause() {
		synchronized(stateChanged) {
			// set the state
			if(isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
			while(true) {
				try {
					stateChanged.wait();		// wait to process the new state
					break;
				} catch(InterruptedException e) {
				}
			}
		}
		wakeLock.release();	// release the wakelock
		glView.onPause();	
		super.onPause();	// pauses the GLSurfaceView and Activity, effectively shutting down the rendering thread and destroying the GLES surface
	}
	
	// returns the instance of the GLGraphics
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}
	
	public Input getInput() {
		return input;
	}
	
	public FileIO getFileIO() {
		return fileIO;
	}
	
	// in case we accidentally try to access the standard graphics instance
	public Graphics getGraphics() {
		throw new IllegalStateException("We are using OpenGL!");
	}
	
	public Audio getAudio() {
		return audio;
	}
	
	public void setScreen(Screen newScreen) {
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		this.screen.pause();
		this.screen.dispose();		// dispose of the current screen
		newScreen.resume();
		newScreen.update(0);		// update the new screen once, then set it as the new screen
		this.screen = newScreen;
	}
	
	public Screen getCurrentScreen() {
		return screen;
	}
}