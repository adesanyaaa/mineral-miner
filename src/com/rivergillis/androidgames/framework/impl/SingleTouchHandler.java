package com.rivergillis.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.rivergillis.androidgames.framework.Pool;
import com.rivergillis.androidgames.framework.Input.TouchEvent;
import com.rivergillis.androidgames.framework.Pool.PoolObjectFactory;

/**
 * This class handles single touch inputs from the UI thread
 * @author jgillis
 *
 */
public class SingleTouchHandler implements TouchHandler {
    boolean isTouched;		// Stores the current state of the touchscreen for one finger
    int touchX;
    int touchY;
    Pool<TouchEvent> touchEventPool;									// Pool that holds instances of touchEvents
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();			// Stores touchEvents to be sent out
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();	// Buffer for touchEvents
    float scaleX;			// Scales touches to a certain res
    float scaleY;

    public SingleTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();						// set up the factory to return TouchEvents
            }            
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);	// set up the pool using the factory
        view.setOnTouchListener(this);	// sets the touch listener

        this.scaleX = scaleX;			// sets the scales
        this.scaleY = scaleY;
    }

    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this) {
            TouchEvent touchEvent = touchEventPool.newObject();	// fetch a new touchEvent
            switch (event.getAction()) {	//set isTouched and the new touchEvent's type based on the touchEvent 
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                isTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                isTouched = true;
                break;
            case MotionEvent.ACTION_CANCEL:                
            case MotionEvent.ACTION_UP:
                touchEvent.type = TouchEvent.TOUCH_UP;
                isTouched = false;
                break;
            }
            
            touchEvent.x = touchX = (int)(event.getX() * scaleX);	// set the touchEvent's (and members') coords to be multiplied by the scales
            touchEvent.y = touchY = (int)(event.getY() * scaleY);
            touchEventsBuffer.add(touchEvent);                      // Add the touchEvent to the buffer
            
            return true;
        }
    }

    // poll the state of the touch
    public boolean isTouchDown(int pointer) {
        synchronized(this) {
            if(pointer == 0)		// only return if the pointer is 0, since we only use single touch here
                return isTouched;
            else
                return false;
        }
    }

    // poll for the x and y coords
    public int getTouchX(int pointer) {
        synchronized(this) {
            return touchX;
        }
    }

    public int getTouchY(int pointer) {
        synchronized(this) {
            return touchY;
        }
    }

    // get the list of touchevents, same as for keyevents
    public List<TouchEvent> getTouchEvents() {
        synchronized(this) {     
            int len = touchEvents.size();
            for( int i = 0; i < len; i++ )
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
