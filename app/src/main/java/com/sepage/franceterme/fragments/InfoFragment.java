package com.sepage.franceterme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sepage.franceterme.R;

/**
 * Created by Balrog on 1/10/14.
 */
public class InfoFragment extends Fragment {

    View view;

    public InfoFragment() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.info_fragment, container, false);
        return view;
    }

}
