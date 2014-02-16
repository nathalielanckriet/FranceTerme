/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.data.db.adapters;


import android.content.Context;
import android.database.Cursor;

import com.sepage.franceterme.data.db.DatabaseHelper;
import com.sepage.franceterme.entities.Domain;
import com.sepage.franceterme.entities.EquivalentTerm;
import com.sepage.franceterme.entities.RelatedTerm;
import com.sepage.franceterme.entities.Subdomain;
import com.sepage.franceterme.entities.Term;
import com.sepage.franceterme.entities.VariantTerm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class TermAdapter {


    private final static int TermID = 0, TermTitle = 1, TermDefinition = 2, TermNotes = 3, DomainTitle = 4,
            SubdomainTitle = 5, EquivalentTitle = 6, EquivalentLanguage = 7, EquivalentCategory = 8,
            EquivalentNote = 9, RelatedTermTitle = 10, RelatedTermStatus = 11, RelatedTermCategory = 12,
            RelatedTermID = 13, RelatedTermLanguage = 14, SeeAlsoTermID = 15, VariantTitle = 16,
            VariantCategory = 17, VariantLanguage = 18, VariantType = 19;
    private static DatabaseHelper database;

    public static Term getTermFromDatabaseByID(Context context, String id) {

        Term term = new Term();
        Map domainHashMap = new HashMap<String, Domain>(), subdomainHashMap = new HashMap<String, Subdomain>(), variantTermHashMap = new HashMap<String, VariantTerm>(),
                equivalentTermHashMap = new HashMap<String, EquivalentTerm>(), relatedTermHashMap = new HashMap<String, RelatedTerm>();
        HashSet<String> seeAlsoTerms = new HashSet<String>();
        database = new DatabaseHelper(context);
        Cursor results = database.findTermByID(id);

        /* ORDER OF RETURNED COLUMNS IN A SELECT QUERY FOR TERMS BY ID
         * 0TermID	1TermTitle	2TermDefinition	3TermNotes	4DomainTitle	5SubdomainTitle	6EquivalentTitle	7EquivalentLanguage	8EquivalentCategory	9EquivalentNote	10RelatedTermTitle
         * 11RelatedTermStatus	12RelatedTermCategory	13RelatedTermID	14RelatedTermLanguage	15SeeAlsoTermID	16VariantTitle	17VariantCategory	18VariantLanguage	19VariantType
         */
        for (int i = 0; i < results.getCount(); i++) { // For each row in the results
            results.moveToPosition(i);

            // Term
            if (!term.getId().equals(results.getColumnName(TermID))) {   // so you don't add the
                term.setId(results.getString(TermID));
                term.setTitle(results.getString(TermTitle));
                term.setDefinition(results.getString(TermDefinition));
                term.setNotes(results.getString(TermNotes));
            }

            // Domain
            if (!results.isNull(DomainTitle)) {
                Domain tempDomain = new Domain().setTitle(results.getString(DomainTitle));
                domainHashMap.put(tempDomain.getTitle(), tempDomain);
            }

            // Subdomain
            if (!results.isNull(SubdomainTitle)) {
                Subdomain tempSubdomain = new Subdomain().setTitle(results.getString(SubdomainTitle));
                subdomainHashMap.put(tempSubdomain.getTitle(), tempSubdomain);
            }

            //Equivalent
            if (!results.isNull(EquivalentTitle)) {
                EquivalentTerm tempEqui = new EquivalentTerm().setTitle(results.getString(EquivalentTitle))
                        .setLanguage(results.getString(EquivalentLanguage)).setCategory(results.getString(EquivalentCategory)).setNote(results.getString(EquivalentNote));
                equivalentTermHashMap.put(tempEqui.getTitle(), tempEqui);
            }

            // Related Term
            if (!results.isNull(RelatedTermTitle)) {
                RelatedTerm tempRelatedTerm = new RelatedTerm().setTitle(results.getString(RelatedTermTitle)).setStatus(results.getString(RelatedTermStatus)).
                        setCategory(results.getString(RelatedTermCategory)).setFranceterme_id(results.getString(RelatedTermID)).setLangage(results.getString(RelatedTermLanguage));
                relatedTermHashMap.put(tempRelatedTerm.getTitle(), tempRelatedTerm);
            }

            // See Also
            if (!results.isNull(SeeAlsoTermID)) {
                seeAlsoTerms.add(results.getString(SeeAlsoTermID));
            }

            // Variant
            if (!results.isNull(VariantTitle)) {
                VariantTerm tempVariant = new VariantTerm().setTitle(results.getString(VariantTitle)).setCategory(results.getString(VariantCategory))
                        .setLanguage(results.getString(VariantLanguage)).setType(results.getString(VariantType));
                variantTermHashMap.put(tempVariant.getTitle(), tempVariant);
            }
        }

        // add domains
        Iterator iterator = domainHashMap.values().iterator();
        while (iterator.hasNext()) {
            term.addDomain((Domain) iterator.next());
        }

        // add subdomains
        iterator = subdomainHashMap.values().iterator();
        while (iterator.hasNext()) {
            term.addSubDomain((Subdomain) iterator.next());
        }

        // add equivalent terms
        iterator = equivalentTermHashMap.values().iterator();
        while (iterator.hasNext()) {
            term.addEquivalentTerm((EquivalentTerm) iterator.next());
        }

        // add variant terms
        iterator = variantTermHashMap.values().iterator();
        while (iterator.hasNext()) {
            term.addVariantTerm((VariantTerm) iterator.next());
        }

        // add related terms
        iterator = relatedTermHashMap.values().iterator();
        while (iterator.hasNext()) {
            term.addRelatedTerm((RelatedTerm) iterator.next());
        }

        // add see also terms
        String[] ids = (String[]) seeAlsoTerms.toArray();
        for (String seealsoId : ids) {
            term.addSeeAlsoTerm(seealsoId);
        }
        return term;
    }


}


