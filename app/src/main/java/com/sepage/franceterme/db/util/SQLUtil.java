/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.db.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLUtil implements SQLHelper {

    public final static int TERM_DOMAIN_TABLE_REF = 1, TERM_SUBDOMAIN_TABLE_REF = 2, TERM_EQUIVALENT_TABLE_REF = 3, TERM_VARIANT_TABLE_REF = 4, TERM_RELATEDTERM_TABLE_REF = 5, SEEALSO_TERM_TABLE_REF = 6;
    public final static String ANALYZE_DATABASE_SCRIPT = "ANALYZE;",

    CREATE_ALLTABLES_SCRIPT =
            "CREATE TABLE SeeAlsoTerm(id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,seealso_id INTEGER NOT NULL, FOREIGN KEY(seealso_id) REFERENCES Term(id), FOREIGN KEY(term_id) REFERENCES Term(id)) ;" +
                    "CREATE TABLE TermDomain(id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,domain_id INTEGER NOT NULL, FOREIGN KEY(domain_id) REFERENCES Domain (id), FOREIGN KEY(term_id) REFERENCES Term(id));" +
                    "CREATE TABLE TermSubdomain (id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,subdomain_id INTEGER NOT NULL, FOREIGN KEY(term_id) REFERENCES Term (id), FOREIGN KEY (subdomain_id) REFERENCES Subdomain(id));" +
                    "CREATE TABLE TermVariant(id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,variant_id INTEGER NOT NULL, FOREIGN KEY(term_id) REFERENCES Term (id), FOREIGN KEY(variant_id) REFERENCES Variant(id));" +
                    "CREATE TABLE TermEquivalent(id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,equivalent_id INTEGER NOT NULL, FOREIGN KEY(term_id) REFERENCES Term (id), FOREIGN KEY(equivalent_id ) REFERENCES Equivalent (id));" +
                    "CREATE TABLE TermRelatedTerm(id INTEGER PRIMARY KEY,term_id INTEGER NOT NULL,relatedterm_id INTEGER NOT NULL, FOREIGN KEY(term_id) REFERENCES Term (id), FOREIGN KEY(relatedterm_id) REFERENCES RelatedTerm(id));" +
                    "CREATE TABLE Term (id INTEGER NOT NULL, title TEXT NOT NULL, definition TEXT NOT NULL, notes TEXT, category TEXT, langage TEXT, PRIMARY KEY (id));" +
                    "CREATE TABLE Domain (id INTEGER PRIMARY KEY, title TEXT NOT NULL);" +
                    "CREATE TABLE Subdomain (id INTEGER PRIMARY KEY, title TEXT NOT NULL);CREATE TABLE Variant (id INTEGER PRIMARY KEY, title TEXT NOT NULL, language TEXT, category TEXT, type text NOT NULL);" +
                    "CREATE TABLE Equivalent (id INTEGER PRIMARY KEY, title TEXT NOT NULL, language TEXT, note TEXT, category TEXT, origin TEXT);" +
                    "CREATE TABLE RelatedTerm(id INTEGER PRIMARY KEY ,title TEXT NOT NULL, status TEXT, category TEXT, franceterme_id TEXT);" +
                    "CREATE UNIQUE INDEX seealsoterm_index ON SeeAlsoTerm(term_id, seealso_id);" +
                    "CREATE UNIQUE INDEX term_domain_id_index ON TermDomain (term_id, domain_id);" +
                    "CREATE UNIQUE INDEX term_subdomain_id_index ON TermSubdomain (term_id,subdomain_id);" +
                    "CREATE UNIQUE INDEX term_variant_id_index ON TermVariant (term_id, variant_id);" +
                    "CREATE UNIQUE INDEX term_equivalent_id_index ON TermEquivalent (term_id, equivalent_id);" +
                    "CREATE UNIQUE INDEX term_related_term_id_index ON TermRelatedTerm (term_id, relatedterm_id);" +
                    "CREATE UNIQUE INDEX term_id_index ON Term (id ASC);CREATE UNIQUE INDEX term_id_title_index ON Term (title ASC, id);" +
                    "CREATE UNIQUE INDEX similar_terms_index ON SeeAlsoTerm (term_id, seealso_id);" +
                    "CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US');" +
                    "INSERT INTO android_metadata VALUES ('en_US');",

    DROP_ALLTABLES_SCRIPT = "DROP TABLE IF EXISTS SeeAlsoTerm;" +
            "DROP TABLE IF EXISTS TermDomain;" +
            "DROP TABLE IF EXISTS TermSubdomain;" +
            "DROP TABLE IF EXISTS TermVariant;" +
            "DROP TABLE IF EXISTS TermEquivalent;" +
            "DROP TABLE IF EXISTS Term;" +
            "DROP TABLE IF EXISTS Domain;" +
            "DROP TABLE IF EXISTS Subdomain;" +
            "DROP TABLE IF EXISTS Variant;" +
            "DROP TABLE IF EXISTS Equivalent;" +
            "DROP TABLE IF EXISTS RelatedTerm;" +
            "DROP TABLE IF EXISTS TermRelatedTerm;" +
            "DROP INDEX IF EXISTS seealsoterm_index ;" +
            "DROP INDEX IF EXISTS term_domain_id_index;" +
            "DROP INDEX IF EXISTS term_subdomain_id_index;" +
            "DROP INDEX IF EXISTS term_variant_id_index;" +
            "DROP INDEX IF EXISTS term_equivalent_id_index;" +
            "DROP INDEX IF EXISTS term_related_term_id_index;" +
            "DROP INDEX IF EXISTS term_id_index;" +
            "DROP INDEX IF EXISTS term_id_title_index;" +
            "DROP INDEX IF EXISTS similar_terms_index;" +
            "DROP TABLE IF EXISTS android_metadata;";


    public static String getSQLInsertCommand(String tableName, List<String> columns, List<String> values) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName).append(" (");  // insert into table (

        for (int i = 0; i < columns.size(); i++) {      // col1, col2, col3, ...)   all columns

            builder.append(columns.get(i));     // appends column name

            if (i < columns.size() - 1) {    // to avoid adding a comma at the very end
                builder.append(",");
            }
        }
        builder.append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {      // val2, val2, val3, ...)   all values

            builder.append("\'" + encodeParameterForSQL(values.get(i)) + "\'");    // encodes the parameter for SQL (escapes single quotes with two single quotes) and appends

            if (i < values.size() - 1) {    // to avoid adding a comma at the very end
                builder.append(",");
            }
        }
        builder.append(");");
        String temp = builder.toString();
        Log.d("SQL INSERT " + tableName, temp);
        return temp;
    }


    public static String generateForeignKeySQLTable(int TABLE_REF, int termID) {

        // INSERT INTO TermDomain (term_id, domain_id) VALUES ('1', (SELECT last_insert_rowid()))

        switch (TABLE_REF) {

            case TERM_DOMAIN_TABLE_REF: {
                return ("INSERT INTO " + TERM_DOMAIN_TABLE + " (" + TERMID_COLUMN + "," + DOMAINID_COLUMN + ") VALUES (\'" + termID + "\'," + LAST_ROWID + ");");
            }
            case TERM_SUBDOMAIN_TABLE_REF: {
                return ("INSERT INTO " + TERM_SUBDOMAIN_TABLE + " (" + TERMID_COLUMN + "," + SUBDOMAINID_COLUMN + ") VALUES (\'" + termID + "\'," + LAST_ROWID + ");");
            }
            case TERM_EQUIVALENT_TABLE_REF: {
                return ("INSERT INTO " + TERM_EQUIVALENT_TABLE + " (" + TERMID_COLUMN + "," + EQUIVALENTID_COLUMN + ") VALUES (\'" + termID + "\'," + LAST_ROWID + ");");
            }
            case TERM_VARIANT_TABLE_REF: {
                return ("INSERT INTO " + TERM_VARIANT_TABLE + " (" + TERMID_COLUMN + "," + VARIANTID_COLUMN + ") VALUES (\'" + termID + "\'," + LAST_ROWID + ");");
            }
            case TERM_RELATEDTERM_TABLE_REF: {
                return ("INSERT INTO " + TERM_RELATEDTERM_TABLE + " (" + TERMID_COLUMN + "," + RELATEDID_COLUMN + ") VALUES (\'" + termID + "\'," + LAST_ROWID + ");");
            }
        }

        return "";
    }

    public static String getSELECTQueryForTermById() {
        return ("SELECT T.id AS TermID, T.title AS TermTitle, T.definition AS TermDefinition, " +
                "T.notes AS TermNotes, D.title AS DomainTitle, S.title AS SubdomainTitle, E.title AS EquivalentTitle, " +
                "E.language AS EquivalentLanguage, E.category AS EquivalentCategory, E.note AS EquivalentNote, " +
                "R.title AS RelatedTermTitle, R.status AS RelatedTermStatus, R.category AS RelatedTermCategory, " +
                "R.franceterme_id AS RelatedTermID, R.langage AS RelatedTermLanguage,Tsee.seealso_id AS SeeAlsoTermID, " +
                "V.title AS VariantTitle, V.category AS VariantCategory, V.language AS VariantLanguage, V.type AS VariantType " +
                "FROM Term AS T LEFT OUTER JOIN TermDomain AS TD ON T.id = TD.term_id " +
                "LEFT OUTER JOIN TermRelatedTerm AS TR ON T.id = TR.term_id " +
                "LEFT OUTER JOIN TermVariant AS TV ON T.id = TV.term_id " +
                "LEFT OUTER JOIN SeeAlsoTerm AS Tsee ON T.id = Tsee.term_id " +
                "LEFT OUTER JOIN TermSubdomain AS TS ON T.id = TS.term_id " +
                "LEFT OUTER JOIN TermEquivalent AS TE ON T.id = TE.term_id " +
                "LEFT OUTER JOIN Domain AS D ON D.id=TD.domain_id " +
                "LEFT OUTER JOIN Subdomain AS S ON S.id = TS.subdomain_id " +
                "LEFT OUTER JOIN Equivalent AS E ON E.id = TE.equivalent_id " +
                "LEFT OUTER JOIN Variant AS V ON V.id = TV.variant_id " +
                "LEFT OUTER JOIN RelatedTerm AS R ON R.id = TR.relatedterm_id WHERE T.id =");
    }


    @Override
    public String getInsertQuery() {
        // useless method. dont implement
        return null;
    }

    @Override
    public String getUpdateQuery() {
        // useless method. dont implement
        return null;
    }

    public static String encodeParameterForSQL(String parameter) {
        return parameter.replaceAll("\'", "\'\'");
    }
}
