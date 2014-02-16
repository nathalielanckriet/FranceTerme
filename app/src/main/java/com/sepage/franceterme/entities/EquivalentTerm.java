/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;

import com.sepage.franceterme.db.util.SQLHelper;
import com.sepage.franceterme.db.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;

public class EquivalentTerm implements SQLHelper<EquivalentTerm> {


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

    @Override
    public String getInsertQuery() {
        List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
        if (title!=null) {
            columns.add(TITLE_COLUMN);
            values.add(title);
        }
        if (language!= null) {
            columns.add(LANGUAGE_COLUMN);
            values.add(language);
        }
        if (note!=null) {
            columns.add(NOTE_COLUMN);
            values.add(note);
        }
        if (category != null) {
            columns.add(CATEGORY_COLUMN);
            values.add(category);
        }
        if (origin!= null) {
            columns.add(ORIGIN_COLUMN);
            values.add(origin);
        }
        return SQLUtil.getSQLInsertCommand(EQUIVALENT_TABLE, columns, values);
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }

    @Override
    public boolean isNull() {
        if (title==null || title.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(EquivalentTerm object) {
        return title.equals(object.getTitle());

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
