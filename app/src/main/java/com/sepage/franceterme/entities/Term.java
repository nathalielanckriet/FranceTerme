/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


import com.sepage.franceterme.db.util.SQLHelper;
import com.sepage.franceterme.db.util.SQLUtil;
import com.sepage.franceterme.xml.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Term implements SQLHelper {

    private String title;
    private String id;
    private String definition;
    private String notes;
    private String category;
    private String langage;
    private List<Domain> domainList;
    private List<Subdomain> subdomainList;
    private List<VariantTerm> variantTermList;
    private List<EquivalentTerm> equivalentTermList;
    private List<String> seeAlsoTermList;
    private List<RelatedTerm> relatedTermList;


    public Term(String title, String id, String definition, String notes,
                List<Domain> domainList, List<Subdomain> subdomainList, List<VariantTerm> variantTermList,
                List<EquivalentTerm> equivalentTermList, List<String> seeAlsoTermList, List<RelatedTerm> relatedTermList) {
        this.seeAlsoTermList = seeAlsoTermList;
        this.title = title;
        this.id = id;
        this.definition = definition;
        this.notes = notes;
        this.relatedTermList = relatedTermList;
        this.domainList = domainList;
        this.subdomainList = subdomainList;
        this.variantTermList = variantTermList;
        this.equivalentTermList = equivalentTermList;
    }

    public Term() {
        domainList = new ArrayList<Domain>();
        subdomainList = new ArrayList<Subdomain>();
        variantTermList = new ArrayList<VariantTerm>();
        seeAlsoTermList = new ArrayList<String>();
        equivalentTermList = new ArrayList<EquivalentTerm>();
        relatedTermList = new ArrayList<RelatedTerm>();
    }


    @Override
    public String getInsertQuery() {
        List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
        if (title != null) {
            columns.add(TITLE_COLUMN);
            values.add(title);
        }
        if (id != null) {
            columns.add(ID_COLUMN);
            values.add(id);
        }
        if (definition != null) {
            columns.add(DEFINITION_COLUMN);
            values.add(definition);
        }
        if (notes != null) {
            columns.add(NOTES_COLUMN);
            values.add(notes);
        }
        if (category != null) {
            columns.add(CATEGORY_COLUMN);
            values.add(category);
        }
        if (langage != null) {
            columns.add(LANGAGE_COLUMN);
            values.add(langage);
        }
        return SQLUtil.getSQLInsertCommand(TERM_TABLE, columns, values);
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }


    public Term addDomain(Domain domain) {
        domainList.add(domain);
        return this;
    }

    public Term addSubDomain(Subdomain subdomain) {
        subdomainList.add(subdomain);
        return this;
    }


    public List<RelatedTerm> getRelatedTermList() {
        return relatedTermList;
    }

    public void setRelatedTermList(List<RelatedTerm> relatedTermList) {
        this.relatedTermList = relatedTermList;
    }

    public Term addVariantTerm(VariantTerm variantTerm) {
        variantTermList.add(variantTerm);
        return this;
    }

    public Term addEquivalentTerm(EquivalentTerm equivalentTerm) {
        equivalentTermList.add(equivalentTerm);
        return this;
    }

    public Term addSeeAlsoTerm(String similarTerm) {
        seeAlsoTermList.add(similarTerm);
        return this;
    }

    public Term addRelatedTerm(RelatedTerm relatedTerm) {
        relatedTermList.add(relatedTerm);
        return this;
    }


    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getDefinition() {
        return definition;
    }

    public List<String> getSeeAlsoTermList() {
        return seeAlsoTermList;
    }

    public String getNotes() {
        return notes;
    }

    public List<Domain> getDomainList() {
        return domainList;
    }

    public List<Subdomain> getSubdomainList() {
        return subdomainList;
    }

    public List<VariantTerm> getVariantTermList() {
        return variantTermList;
    }

    public List<EquivalentTerm> getEquivalentTermList() {
        return equivalentTermList;
    }

    public Term setTitle(String title) {
        this.title = title;
        return this;
    }

    public Term setId(String id) {
        this.id = id;
        return this;
    }

    public Term setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public Term setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Term setDomainList(List<Domain> domainList) {
        this.domainList = domainList;
        return this;
    }

    public Term setSubdomainList(List<Subdomain> subdomainList) {
        this.subdomainList = subdomainList;
        return this;
    }

    public Term setVariantTermList(List<VariantTerm> variantTermList) {
        this.variantTermList = variantTermList;
        return this;
    }

    public Term setEquivalentTermList(List<EquivalentTerm> equivalentTermList) {
        this.equivalentTermList = equivalentTermList;
        return this;
    }

    public Term setSeeAlsoTermList(List<String> seeAlsoTermList) {
        this.seeAlsoTermList = seeAlsoTermList;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Term setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getLangage() {
        return langage;
    }

    public Term setLangage(String langage) {
        this.langage = langage;
        return this;

    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Term Title:\t\t\t\t" + title);
        builder.append("\nTerm id:\t\t\t\t" + id);
        builder.append("\nTerm Definition:\t\t" + definition);
        builder.append("\nTerm Notes:\t\t\t\t" + notes);
        builder.append("\nTerm Category:\t\t\t" + category);
        builder.append("\nTerm Langage:\t\t\t" + langage);
        builder.append("\nTerm Domains:\t\t\t" + Util.listToString(domainList));
        builder.append("\nTerm Subdomains:\t\t" + Util.listToString(subdomainList));
        builder.append("\nTerm Variants:\t\t\t" + Util.listToString(variantTermList));
        builder.append("\nTerm Equivalents:\t\t" + Util.listToString(equivalentTermList));
        builder.append("\nTerm SeeAlsoTerms:\t\t" + Util.listToStringNoBreak(seeAlsoTermList));
        builder.append("\nTerm RelatedTerms:\t\t" + Util.listToString(relatedTermList));
        return builder.toString();
    }

}
