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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sepage.franceterme.R;
import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.data.db.adapters.TermAdapter;
import com.sepage.franceterme.entities.Domain;
import com.sepage.franceterme.entities.EquivalentTerm;
import com.sepage.franceterme.entities.Subdomain;
import com.sepage.franceterme.entities.Term;
import com.sepage.franceterme.entities.VariantTerm;
import com.sepage.franceterme.view.activities.MainActivity;
import com.sepage.franceterme.view.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DiscoverTermsFragment extends Fragment implements View.OnClickListener {

    private View view, collapsedTermContainer, mainTermContainer;
    private EditText searchBar;
    private AutoCompleteTextView searchAutocomplete;
    private Term currentTerm;
    private boolean buttonsAreDisplayed = false;

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
        final HashMap<String, String> titleIDs = DataPool.getTitleIDHashMap(), idTitles = DataPool.getIDTitleHashMap();
        mainTermContainer = view.findViewById(R.id.main_term_container);
        searchBar = (EditText) getActivity().findViewById(R.id.discoverterms_search_autocomplete);
        collapsedTermContainer = getActivity().findViewById(R.id.collapsedTermContainer);
        searchAutocomplete = (AutoCompleteTextView) view.findViewById(R.id.discoverterms_search_autocomplete);

        currentTerm = DataPool.getRandomTerm();
        displayRequestedTermCollapsed(currentTerm,1);
        final DiscoverTermsFragment temp = this;

        searchAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String selectedTerm = (String) parent.getItemAtPosition(pos);
                Log.d("Autocomplete selection", "Selected term: /" + selectedTerm + "/ from autocomplete");
                String termID = titleIDs.get(selectedTerm);
                currentTerm = TermAdapter.getTermFromDatabaseByID(getActivity(), termID);
                clearAllTerms();
                displayRequestedTermCollapsed(currentTerm, 0);
            }
        });
        return view;
    }


    /**
     * Inflates the collapsed term container, populates it with data and adds it to the parent
     *
     * @param term
     * @param position
     */
    public void displayRequestedTermCollapsed(Term term, int position) {
        View collapsedTerm = getActivity().getLayoutInflater().inflate(R.layout.term_display_collapsed, null);
        ((TextView) collapsedTerm.findViewById(R.id.term_title_collapsed)).setText(term.getTitle());    // title

        String langage = term.getLangage();
        String category = term.getCategory();
        ((TextView) collapsedTerm.findViewById(R.id.term_type_langage_collapsed))
                .setText("" + (category != null ? ", " + category : "") + (langage != null ? ", " + langage : "")); //category and langage if any

        StringBuilder builder = new StringBuilder();
        List<Domain> domains = term.getDomainList();
        for (int i = 0; i < domains.size(); i++) {
            builder.append(domains.get(i).getTitle().toUpperCase());
            if (i != domains.size() - 1 && domains.size() > 1) {
                builder.append(" - ");
            }
        }
        List<Subdomain> subdomains = term.getSubdomainList();
        if (subdomains.size() > 1) {   // so you dont add the separator
            builder.append(" / ");
        }
        for (int i = 0; i < subdomains.size(); i++) {
            builder.append(subdomains.get(i).getTitle().toUpperCase());
            if (i != subdomains.size() - 1 && subdomains.size() > 1) {
                builder.append(" - ");
            }
        }
        ((TextView) collapsedTerm.findViewById(R.id.term_domains_subdomains_collapsed)).setText(builder.toString());
        builder = new StringBuilder();

        if (term.getDefinition()!=null && term.getDefinition().length() > 40) {
            ((TextView) collapsedTerm.findViewById(R.id.term_definition_short_collapsed)).setText(term.getDefinition().substring(0, 40) + "...");
        } else {
            ((TextView) collapsedTerm.findViewById(R.id.term_definition_short_collapsed)).setText(term.getDefinition());
        }

        List<EquivalentTerm> equivalentTerms = term.getEquivalentTermList();
        for (int i = 0; i < equivalentTerms.size(); i++) {
            builder.append(equivalentTerms.get(i).getTitle());
            if (i != equivalentTerms.size() - 1 && equivalentTerms.size() > 1) {
                builder.append(", ");
            }
        }
        ((TextView) collapsedTerm.findViewById(R.id.term_equivalents_collapsed)).setText(builder.toString());

        collapsedTerm.setOnClickListener(this);

        ViewUtil.addViewToParent((ViewGroup) view.findViewById(R.id.main_term_container), collapsedTerm, position);
        if (!buttonsAreDisplayed) {

        }
    }


    public void displayRequestedTermExpanded(Term term, int position) {
        View expandedTerm = getActivity().getLayoutInflater().inflate(R.layout.term_display_expanded, null);
        TableLayout table = (TableLayout) expandedTerm.findViewById(R.id.term_display_expanded_table);
        List<TableRow> rows = new ArrayList<TableRow>();
        TableRow temp;
        TextView label;
        TextView data;

        // term title
        ((TextView) expandedTerm.findViewById(R.id.term_expanded_title)).setText(term.getTitle());

        // langage and category
        String langage = term.getLangage();
        String category = term.getCategory();
        ((TextView) expandedTerm.findViewById(R.id.term_expanded_type_langage))
                .setText("" + (category != null ? ", " + category : "") + (langage != null ? ", " + langage : "")); //category and langage if any


        // Variants
        List<VariantTerm> variantTerms = term.getVariantTermList();
        for (VariantTerm variantTerm : variantTerms) {
            temp = new TableRow(getActivity());
            label = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
            data = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
            if (variantTerm.getType() != null) {
                label.setText(variantTerm.getType());
            } else {
                label.setText("Variante");
            }
            data.setText(variantTerm.getTitle());
            temp.addView(label);
            temp.addView(data);
            rows.add(temp);
        }

        // domains and subdomains
        StringBuilder builder = new StringBuilder();
        List<Domain> domains = term.getDomainList();
        for (int i = 0; i < domains.size(); i++) {
            builder.append(domains.get(i).getTitle().toUpperCase());
            if (i != domains.size() - 1 && domains.size() > 1) {
                builder.append(" - ");
            }
        }
        if (builder.length() > 1) {   // so you dont add the separator
            builder.append(" / ");
        }
        List<Subdomain> subdomains = term.getSubdomainList();
        for (int i = 0; i < subdomains.size(); i++) {
            builder.append(subdomains.get(i).getTitle().toUpperCase());
            if (i != subdomains.size() - 1 && subdomains.size() > 1) {
                builder.append(" - ");
            }
        }
        temp = new TableRow(getActivity());
        label = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        data = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        label.setText("Domaine");
        data.setText(builder.toString());
        temp.addView(label);
        temp.addView(data);
        rows.add(temp);
        builder = new StringBuilder();

        // Definition
        if (term.getDefinition() != null && !term.getDefinition().isEmpty()) {
            temp = new TableRow(getActivity());
            label = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
            data = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
            label.setText("Définition");
            data.setText(term.getDefinition());
            temp.addView(label);
            temp.addView(data);
            rows.add(temp);
        }


        // Equivalents
        List<EquivalentTerm> equivalentTerms = term.getEquivalentTermList();
        for (int i = 0; i < equivalentTerms.size(); i++) {
            builder.append(equivalentTerms.get(i).getTitle() + " (" + equivalentTerms.get(i).getLanguage() + ")");
            if (i != equivalentTerms.size() - 1 && equivalentTerms.size() > 1) {
                builder.append(", ");
            }
        }

        temp = new TableRow(getActivity());
        label = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        data = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        label.setText("Équivalent étranger");
        data.setText(builder.toString());
        temp.addView(label);
        temp.addView(data);
        rows.add(temp);
        builder = new StringBuilder();

        HashMap<String,String> idTitle = DataPool.getIDTitleHashMap();
        // See Also
        List<String> seeAlsoTerms = term.getSeeAlsoTermList();
        for (int i = 0; i < seeAlsoTerms.size(); i++) {
            builder.append(idTitle.get(seeAlsoTerms.get(i)));
            if (i != seeAlsoTerms.size() - 1 && seeAlsoTerms.size() > 1) {
                builder.append(", ");
            }
        }
        temp = new TableRow(getActivity());
        label = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        data = (TextView) getActivity().getLayoutInflater().inflate(R.layout.term_tableview_label, null);
        label.setText("Voir aussi");
        data.setText(builder.toString());
        temp.addView(label);
        temp.addView(data);
        rows.add(temp);
        builder = new StringBuilder();


        expandedTerm.setOnClickListener(this);
        table.setOnClickListener(this);

        for (int i=0; i<rows.size(); i++) {
            table.addView(rows.get(i),table.getChildCount());
        }

        ViewUtil.addViewToParent((ViewGroup)getActivity().findViewById(R.id.main_term_container), expandedTerm, 0);
    }


    private void clearAllTerms() {
        ViewUtil.removeAllChildren((ViewGroup) getActivity().findViewById(R.id.main_term_container));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Autocomplete", "Loading " + DataPool.getIDTitleHashMap().size() + " entries to autocomplete");
        Object[] temp = DataPool.getIDTitleHashMap().values().toArray();
        String[] data = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            data[i] = (String) temp[i];
        }
        searchAutocomplete.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.term_display_collapsed: {
                Log.d("Click", "tap on collapsed term");
                clearAllTerms();
                ((ViewGroup)getActivity().findViewById(R.id.main_term_container)).addView(getActivity().getLayoutInflater().inflate(R.layout.term_notfound_buttons, null));
                setMargins(getActivity().findViewById(R.id.discoverterms_search_autocomplete), 0, 0, 0, 0);
                displayRequestedTermExpanded(currentTerm, 0);
                break;
            }
            case R.id.term_display_expanded: {
                Log.d("Click", "tap on expanded term");
                clearAllTerms();
                setMargins(getActivity().findViewById(R.id.discoverterms_search_autocomplete), 0, 90, 0, 0);
                displayRequestedTermCollapsed(currentTerm, 0);
                break;
            }
            case R.id.term_display_expanded_table: {
                Log.d("Click", "tap on expanded term: TABLE");
                onClick(view.findViewById(R.id.term_display_expanded));
                break;
            }
            case R.id.discover_suggestterm_button: {
                MainActivity.selectTab(MainActivity.TAB_SUGGEST_INDEX);
            }
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
