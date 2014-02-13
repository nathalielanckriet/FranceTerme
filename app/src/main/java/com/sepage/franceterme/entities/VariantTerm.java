/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


public class VariantTerm {


    private String title;
    private String category;
    private String language;
    private String type;
    private String sqlid;

    public VariantTerm() {
    }

    public VariantTerm(String title, String sqlid, String language, String category) {
        this.sqlid = sqlid;
        this.title = title;
        this.language = language;
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public VariantTerm setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public VariantTerm setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getSqlid() {
        return sqlid;
    }

    public VariantTerm setSqlid(String sqlid) {
        this.sqlid = sqlid;
        return this;
    }

    public String getTitle () {
        return title;
    }

    public VariantTerm setTitle (String title) {
        this.title = title;
        return this;
    }

    public String getType() {
        return type;
    }

    public VariantTerm setType(String type) {
        this.type = type;
        return this;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title:"+title+", cat:"+category+", lang:"+language+", type:"+type);
        return builder.toString();
    }

}
