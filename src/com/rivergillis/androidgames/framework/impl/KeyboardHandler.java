package com.rivergillis.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.rivergillis.androidgames.framework.Pool;
import com.rivergillis.androidgames.framework.Input.KeyEvent;
import com.rivergillis.androidgames.framework.Pool.PoolObjectFactory;

/**
 * Connects with the View from which keyboard events are to be received, and
 * stores the current state of each key for polling, keeps a list of keyEvent instances
 * and properly synchronizes everything
 * @author jgillis
 *
 */
public class KeyboardHandler implements OnKeyListener {
    boolean[] pressedKeys = new boolean[128];					// store the state of each key in this array, indexed by key code
    Pool<KeyEvent> keyEventPool;								// Pool that holds instances of our KeyEvent class
    List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>(); // Stores KeyEvent instances that haven't yet been consumed by the game
    List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();		// Stores KeyEvents that we return by calling getKeyEvents()
    
    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {	// create the poolObjectFactory
            public KeyEvent createObject() {
                return new KeyEvent();						// createObjects() will return a keyEvent
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);	// create the pool using the new factory
        view.setOnKeyListener(this);						// set the onKeyEventListener
        view.setFocusableInTouchMode(true);					// make it focusable and set it as the focus
        view.requestFocus();
    }

    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)	// ignore key events that encode a KeyEvnet.ACTION_MULTIPLE event
            return false;

        synchronized (this) {	// events are received on the UI thread and read on the main loop thread, makes sure no members are accessed in parallel
            KeyEvent keyEvent = keyEventPool.newObject();		// fetch a keyEvent instance from the pool
            keyEvent.keyCode = keyCode;							// set the keyEvent's keycode
            keyEvent.keyChar = (char) event.getUnicodeChar();	// set the keyEvent's keyChar
            
            // Set the new keyEvent's type and whether it was pressed or not in the pressedKeys array  
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);		// Add the keyEvent to the buffer
        }
        return false;
    }

    // returns whether or not the key with the value of the passed-in keycode is pressed
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));	// loop through the keyEvents array and insert all of its KeyEvents into our pool
            }
            keyEvents.clear();					// clear the keyEvents list
            keyEvents.addAll(keyEventsBuffer);	// fill the list with the event sin the buffer
            keyEventsBuffer.clear();			// clear the buffer
            return keyEvents;					// return the keyevents list
        }
    }
}
