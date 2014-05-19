package com.rivergillis.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.view.MotionEvent;
import android.view.View;

import com.rivergillis.androidgames.framework.Pool;
import com.rivergillis.androidgames.framework.Input.TouchEvent;
import com.rivergillis.androidgames.framework.Pool.PoolObjectFactory;

/**
 * This class handles multi-touch events from the UI thread
 * @author jgillis
 *
 */
@TargetApi(5)
public class MultiTouchHandler implements TouchHandler {
    private static final int MAX_TOUCHPOINTS = 10;
	
    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];		// contains the state of 10 fingers
    int[] touchX = new int[MAX_TOUCHPOINTS];				// contains the coords of 10 fingers
    int[] touchY = new int[MAX_TOUCHPOINTS];
    int[] id = new int[MAX_TOUCHPOINTS];					// contains the ids of 10 fingers
    Pool<TouchEvent> touchEventPool;									// pool of touch events
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();			// touch events to be returned to game
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();	// touch events not yet consumed by game
    float scaleX;
    float scaleY;		// used for scaling the input

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();						// sets up the factory to return touchEvents
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);	// sets up the pool using the factory
        view.setOnTouchListener(this);	// set the touch listener

        this.scaleX = scaleX;
        this.scaleY = scaleY;			// set the scales
    }

    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;	// get an action from the event using the motionevent actionmask
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;	// get the pointerindex
            int pointerCount = event.getPointerCount();	// get the pointer count from the event
            TouchEvent touchEvent;		// create a touchevent
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                if (i >= pointerCount) {
                    isTouched[i] = false;				// set the isTouched and id to false after we are bigger than the pointer count
                    id[i] = -1;
                    continue;
                }
                int pointerId = event.getPointerId(i);	// get the pointer id of the current iteration
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    // if it's an up/down/cancel/out event, mask the id to see if we should process it for this touchpoint
                    continue;
                }
                switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();	// fetch a new touch event, set the type, id, and scale, set the members, and add it to the buffer
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = true;
                    id[i] = pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();	// fetch a new touch event, set the type, id, and scale, set the members, and add it to the buffer
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = false;
                    id[i] = -1;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_MOVE:
                    touchEvent = touchEventPool.newObject();	// fetch a new touch event, set the type, id, and scale, set the members, and add it to the buffer
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = true;
                    id[i] = pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;
                }
            }
            return true;
        }
    }

    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return isTouched[index];		// if no errors with the pointer and index, send back the isTouched of the index
        }
    }

    public int getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];			// send back the x and y values for the index
        }
    }

    public int getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    // return the touchEvents, exactly the same as singleTouchHandler
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
    
    // returns the index for a given pointerId or -1 if no index.
    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            if (id[i] == pointerId) {
                return i;
            }
        }
        return -1;
    }
}
