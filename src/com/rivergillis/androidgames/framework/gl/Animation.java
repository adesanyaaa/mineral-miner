package com.rivergillis.androidgames.framework.gl;

/**
 * This class stores the data for a single animation
 * @author jgillis
 *
 */
public class Animation {
	public static final int ANIMATION_LOOPING = 0;		// animation begins again after the final frame
	public static final int ANIMATION_NONLOOPING = 1;	// animation stops at the last frame
	
	final TextureRegion[] keyFrames;	// holds the frames of the animation
	final float frameDuration;			// duration of the frame
	
	public Animation(float frameDuration, TextureRegion ... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}
	
	public TextureRegion getKeyFrame(float stateTime, int mode) {
		int frameNumber = (int)(stateTime / frameDuration);				// calculate how many frames have been played based on stateTime
		if(mode == ANIMATION_NONLOOPING) {	
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);	// clamp the frameNumber to the last element in the keyFrames array if it is nonlooping
		} else {
			frameNumber = frameNumber % keyFrames.length;				// if it loops, we take the modulus, which will automatically create the looping effect
		}
		return keyFrames[frameNumber];		// return the proper textureregion
	}
}
