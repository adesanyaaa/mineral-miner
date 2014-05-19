package com.rivergillis.androidgames.framework.impl;

import java.util.List;

import android.view.View.OnTouchListener;

import com.rivergillis.androidgames.framework.Input.TouchEvent;

/**
 * This interface is used by both Single and MultiTouchHandler and is used for basic information
 * @author jgillis
 *
 */
public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<TouchEvent> getTouchEvents();
}
