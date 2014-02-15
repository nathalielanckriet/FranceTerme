package com.sepage.franceterme.view.util;

/**
 * Created by Balrog on 1/14/14.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {

    public static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    public static View replaceView(LayoutInflater inflater, View currentView, int newViewId) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return null;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        View newView = inflater.inflate(newViewId, parent, false);
        parent.addView(newView, index);
        return newView;
    }


    public static void addViewToParent(LayoutInflater inflater, ViewGroup parent, int viewToAdd) {
        addViewToParent(inflater, parent, viewToAdd, 0);
    }


    public static void addViewToParent(LayoutInflater inflater, ViewGroup parent, int viewToAdd, int index) {
        parent.addView(inflater.inflate(viewToAdd, parent, false), index);
    }
}