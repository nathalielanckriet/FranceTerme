package com.sepage.franceterme.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sepage.franceterme.R;

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

        TextView infoPage = (TextView) view.findViewById(R.id.infoPageTextView);            // set HTML
        infoPage.setText(Html.fromHtml(getResources().getString(R.string.info_page_text)));

        return view;
    }

}
