/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;

public class EquivalentTerm {

    private String title;
    private String language;
    private String sqlid;
    private String note;
    private String category;
    private String origin;

    public EquivalentTerm () {}

    public EquivalentTerm (String sqlid, String language, String title, String note, String origin, String category) {
        this.sqlid = sqlid;
        this.language = language;
        this.title = title;
        this.origin = origin;
        this.category = category;
        this.note = note;
    }

    public String getLanguage () {
        return language;
    }

    public EquivalentTerm setLanguage (String language) {
        this.language = language;
        return this;
    }

    public String getTitle () {
        return title;
    }

    public EquivalentTerm setTitle (String title) {
        this.title = title;
        return this;
    }

    public String getSqlid () {
        return sqlid;
    }

    public EquivalentTerm setSqlid (String sqlid) {
        this.sqlid = sqlid;
        return this;
    }

    public String getOrigin () {
        return origin;
    }

    public String getNote () {
        return note;
    }

    public EquivalentTerm setNote (String note) {
        this.note = note;
        return this;
    }

    public String getCategory () {
        return category;
    }

    public EquivalentTerm setCategory (String category) {
        this.category = category;
        return this;
    }


    public EquivalentTerm setOrigin (String origin) {
        this.origin = origin;
        return this;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append("title:" + title + ", cat:" + category + ", lang:" + language + ", origin:" + origin+", note:"+note);
        return builder.toString();
    }

}
