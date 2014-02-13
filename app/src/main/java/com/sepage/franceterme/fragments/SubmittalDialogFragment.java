package com.sepage.franceterme.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.sepage.franceterme.R;


public class SubmittalDialogFragment extends DialogFragment {

    View view;

    public SubmittalDialogFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = inflater.inflate(R.layout.proposeterm_dialogfragment_layout, container, false);
        builder.setView(view);
        ((ImageButton) view.findViewById(R.id.proposeterm_dialog_acceptbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
         getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;

//        builder.setMessage(R.string.thankyou_submitalert_text)
//                .setPositiveButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        Dialog dialog = builder.create();
//        Button button = (Button) dialog.findViewById(android.R.id.button1);
//        button.setBackgroundResource(R.drawable.ic_action_accept);
//        return dialog;
    }
}
