package com.iguxuan.iguxuan_friends.friends.model;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;

/**
 * @author nanck on 2016/8/2.
 */
public abstract class BasePost {

    ViewManager vm = new ViewManager() {
        @Override
        public void addView(View view, ViewGroup.LayoutParams layoutParams) {

        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams) {

        }

        @Override public void removeView(View view) {

        }
    };

    WindowManager wm = new WindowManager() {
        @Override public Display getDefaultDisplay() {
            return null;
        }

        @Override public void removeViewImmediate(View view) {

        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams layoutParams) {

        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams) {

        }

        @Override public void removeView(View view) {

        }
    };
}
