/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.data.db.adapters;


import android.content.Context;
import android.database.Cursor;

import com.sepage.franceterme.data.DataPool;
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


    private final static int TermID = 0, TermTitle = 1, TermDefinition = 2, TermCategory = 3, TermLangage = 4,
            TermNotes = 5, DomainTitle = 6, SubdomainTitle = 7, EquivalentTitle = 8 ,EquivalentLanguage = 9,
            EquivalentCategory = 10, EquivalentNote = 11, RelatedTermTitle = 12, RelatedTermStatus = 13, RelatedTermCategory = 14,
            RelatedTermID = 15, RelatedTermLanguage = 16, SeeAlsoTermID = 17, VariantTitle = 18,
            VariantCategory = 19, VariantLanguage = 20, VariantType = 21;

    public static Term getTermFromDatabaseByID(Context context, String id) {
        Term term = new Term();
        Map domainHashMap = new HashMap<String, Domain>(), subdomainHashMap = new HashMap<String, Subdomain>(), variantTermHashMap = new HashMap<String, VariantTerm>(),
                equivalentTermHashMap = new HashMap<String, EquivalentTerm>(), relatedTermHashMap = new HashMap<String, RelatedTerm>();
        HashSet<String> seeAlsoTerms = new HashSet<String>();
        Cursor results = DataPool.getDatabaseHelper().findTermByID(id);

        /* ORDER OF RETURNED COLUMNS IN A SELECT QUERY FOR TERMS BY ID
         * 0TermID	1TermTitle	2TermDefinition	3TermCategory  4TermLangage 5TermNotes	6DomainTitle	7SubdomainTitle	8EquivalentTitle	9EquivalentLanguage	10EquivalentCategory
         * 11EquivalentNote	12RelatedTermTitle 13RelatedTermStatus	14RelatedTermCategory	15RelatedTermID	16RelatedTermLanguage	17SeeAlsoTermID	18VariantTitle	19VariantCategory
         * 20VariantLanguage	21VariantType
         */
        for (int i = 0; i < results.getCount(); i++) { // For each row in the results
            results.moveToPosition(i);

            // Term
            if (i == 0 || !term.getId().equals(results.getColumnName(TermID))) {   // so you don't add the same term multiple times
                term.setId(results.getString(TermID));
                term.setTitle(results.getString(TermTitle));
                term.setDefinition(results.getString(TermDefinition));
                term.setCategory(results.getString(TermCategory));
                term.setLangage(results.getString(TermLangage));
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
        for (Object seealsoId : seeAlsoTerms.toArray()) {
            term.addSeeAlsoTerm((String) seealsoId);
        }
        results.close();
        return term;
    }


}


