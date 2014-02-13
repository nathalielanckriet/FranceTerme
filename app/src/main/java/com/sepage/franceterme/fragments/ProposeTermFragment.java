package com.sepage.franceterme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sepage.franceterme.R;

/**
 * Created by Balrog on 1/10/14.
 */
public class ProposeTermFragment extends Fragment implements View.OnClickListener {

    View view;

    public ProposeTermFragment() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.proposeterm_fragment, container, false);
        view.findViewById(R.id.proposeterm_submit_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proposeterm_submit_button: {
                new SubmittalDialogFragment().show(getFragmentManager(), "");
            }
        }
    }
}
