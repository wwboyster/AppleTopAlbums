package com.quarkworks.appletopalbums.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.quarkworks.appletopalbums.interfaces.ClickListener;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context pContext, final RecyclerView pRecyclerView, final ClickListener pClickListener) {
        clickListener = pClickListener;
        gestureDetector = new GestureDetector(pContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = pRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, pRecyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View lChild = rv.findChildViewUnder(e.getX(), e.getY());
        if (lChild != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(lChild, rv.getChildPosition(lChild));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean pDisallowIntercept) {

    }
}