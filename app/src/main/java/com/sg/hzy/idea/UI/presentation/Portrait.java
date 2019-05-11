package com.sg.hzy.idea.UI.presentation;

import android.animation.Animator;
import android.view.ViewGroup;

public interface Portrait {
    Animator showHalo();
    Animator hideHalo();
    void want(boolean want);
    void desire(boolean desire);
    int getLeft();
    int getTop();
    int getWidth();
    int getHeight();
    float getX();
    float getY();
    ViewGroup.LayoutParams getLayoutParams();
    int getDesireLeft();
    int getDesireRight();
    int getDesireTop();
    int getDesireBottom();
}
