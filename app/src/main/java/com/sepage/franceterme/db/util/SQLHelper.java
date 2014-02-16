/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.db.util;


import java.util.List;

public interface SQLHelper<T> {

    public final static String DOMAIN_TABLE="Domain", TERM_TABLE="Term", SUBDOMAIN_TABLE="Subdomain", VARIANT_TABLE="Variant", RELATEDTERM_TABLE="RelatedTerm",
            EQUIVALENT_TABLE="Equivalent", TERM_DOMAIN_TABLE="TermDomain", TERM_SUBDOMAIN_TABLE="TermSubdomain", TERM_VARIANT_TABLE="TermVariant",
            TERM_RELATEDTERM_TABLE="TermRelatedTerm", TERM_EQUIVALENT_TABLE="TermEquivalent", SEEALSO_TABLE="SeeAlsoTerm",
            TITLE_COLUMN="title", ID_COLUMN="_id", FRANCETERMEID_COLUMN="franceterme_id", LANGUAGE_COLUMN="language" , TYPE_COLUMN="type",
            CATEGORY_COLUMN="category" , LANGAGE_COLUMN="langage", STATUS_COLUMN="status", TERMID_COLUMN="term_id", DOMAINID_COLUMN="domain_id",
            SUBDOMAINID_COLUMN="subdomain_id", VARIANTID_COLUMN="variant_id", EQUIVALENTID_COLUMN="equivalent_id", RELATEDID_COLUMN="relatedterm_id" , NOTE_COLUMN="note",
            NOTES_COLUMN="notes" , DEFINITION_COLUMN="definition", ORIGIN_COLUMN="origin" , SEEALSOID_COLUMN="seealso_id", LAST_ROWID="(SELECT last_insert_rowid())" ;

    public String getInsertQuery();

    public String getUpdateQuery();

    public boolean isNull() ;

    public boolean equals(T object) ;


}
