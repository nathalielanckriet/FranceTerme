package com.sepage.franceterme.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.sepage.franceterme.R;
import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.view.activities.MainActivity;
import com.sepage.franceterme.view.util.ViewUtil;

import java.util.Collection;
import java.util.HashMap;


public class DiscoverTermsFragment extends Fragment implements View.OnClickListener {

    private View view, termsParentView;
    private EditText searchBar;
    private AutoCompleteTextView searchAutocomplete;

    public DiscoverTermsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.discoverterms_fragment, container, false);
        final LayoutInflater inflater1 = inflater;
        searchBar = (EditText) getActivity().findViewById(R.id.discoverterms_search_autocomplete);

        termsParentView = getActivity().findViewById(R.id.term_du_moment_container);
        View collapsedTerm = view.findViewById(R.id.discover_collapsed_term);
        collapsedTerm.setOnClickListener(this);

        searchAutocomplete = (AutoCompleteTextView) view.findViewById(R.id.discoverterms_search_autocomplete);

        final HashMap<String,String> titleIDs = DataPool.getIDTitleHashMap();
        searchAutocomplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                Log.d("Autocomplete", "autocomplete position clicked: "+pos+", is term: ");
                ViewUtil.replaceView(inflater1, view.findViewById(R.id.term_du_moment_container), R.layout.search_term_collapsed_layout);

                view.findViewById(R.id.search_term_collapsed_layout).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View newView = ViewUtil.replaceView(getActivity().getLayoutInflater(), v, R.layout.search_term_expanded_layout);
                        newView.setOnClickListener(this);
                    }
                });

                Button suggestTermButton = (Button) view.findViewById(R.id.discover_suggestterm_button);
                suggestTermButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.selectTab(MainActivity.TAB_SUGGEST_INDEX);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Autocomplete", "Loading "+DataPool.getIDTitleHashMap().size()+" entries to autocomplete");
        Object[] temp = DataPool.getIDTitleHashMap().values().toArray();
        String[] data = new String[temp.length];
        for (int i=0; i<temp.length; i++) {
            data[i] = (String) temp[i];
        }
        searchAutocomplete.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));
    }

    private void setOnClick(View v) {
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_collapsed_term: {
                View newView = ViewUtil.replaceView(getActivity().getLayoutInflater(), v, R.layout.ecotaxe_term_expanded_layout);
                newView.setOnClickListener(this);
                break;
            }
            case R.id.term_expanded_layout: {
                View newView = ViewUtil.replaceView(getActivity().getLayoutInflater(), v, R.layout.ecotaxe_term_collapsed_layout);
                newView.setOnClickListener(this);
                break;
            }
            case R.id.search_term_collapsed_layout: {
                View newView = ViewUtil.replaceView(getActivity().getLayoutInflater(), v, R.layout.search_term_expanded_layout);
                newView.setOnClickListener(this);
                break;
            }
            case R.id.search_term_expanded_layout: {
                View newView = ViewUtil.replaceView(getActivity().getLayoutInflater(), v, R.layout.search_term_collapsed_layout);
                newView.setOnClickListener(this);
                break;
            }
            case R.id.discoverterms_search_autocomplete: {
                ViewUtil.removeView(view.findViewById(R.id.discover_termdumoment_textview));
                break;
            }
            case R.id.discover_suggestterm_button: {
                MainActivity.selectTab(MainActivity.TAB_SUGGEST_INDEX);
            }
        }
    }
}
