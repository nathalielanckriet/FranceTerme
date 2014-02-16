/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


import com.sepage.franceterme.data.db.util.SQLHelper;
import com.sepage.franceterme.data.db.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;

public class VariantTerm implements SQLHelper {


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

    @Override
    public String getInsertQuery() {
        List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
        if (title != null) {
            columns.add(TITLE_COLUMN);
            values.add(title);
        }
        if (category != null) {
            columns.add(CATEGORY_COLUMN);
            values.add(category);
        }
        if (language != null) {
            columns.add(LANGUAGE_COLUMN);
            values.add(language);
        }
        if (type != null) {
            columns.add(TYPE_COLUMN);
            values.add(type);
        }

        return SQLUtil.getSQLInsertCommand(VARIANT_TABLE, columns, values);
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }

    @Override
    public boolean isNull() {
        if (title == null || title.isEmpty()) {
            return true;
        }
        return false;
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

    public String getTitle() {
        return title;
    }

    public VariantTerm setTitle(String title) {
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
        builder.append("title:" + title + ", cat:" + category + ", lang:" + language + ", type:" + type);
        return builder.toString();
    }


    public boolean equals(VariantTerm object) {
        return title.equals(object.getTitle());
    }


}
