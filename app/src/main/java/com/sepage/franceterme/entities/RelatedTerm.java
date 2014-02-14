/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.entities;

import com.sepage.franceterme.db.util.SQLHelper;
import com.sepage.franceterme.db.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;

public class RelatedTerm implements SQLHelper {

    public String sqlid, title, franceterme_id,
            category,   // n.m., etc
            status,     // antonyme, synonyme
            langage;


    @Override
    public String getInsertQuery() {
        List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
        if (title != null ){
            columns.add(TITLE_COLUMN);
            values.add(title);
        }
        if (franceterme_id != null ){
            columns.add(FRANCETERMEID_COLUMN);
            values.add(franceterme_id);
        }
        if (category != null) {
            columns.add(CATEGORY_COLUMN);
            values.add(category);
        }
        if (status != null) {
            columns.add(STATUS_COLUMN);
            values.add(status);
        }
        if (langage != null) {
            columns.add(LANGAGE_COLUMN);
            values.add(langage);
        }
        return SQLUtil.getSQLInsertCommand(RELATEDTERM_TABLE, columns, values);
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }


    public RelatedTerm () {}

    public String getSqlid () {
        return sqlid;
    }

    public RelatedTerm setSqlid (String sqlid) {
        this.sqlid = sqlid;
        return this;
    }

    public String getTitle () {
        return title;
    }

    public RelatedTerm setTitle (String title) {
        this.title = title;
        return this;
    }

    public String getFranceterme_id () {
        return franceterme_id;
    }

    public RelatedTerm setFranceterme_id (String franceterme_id) {
        this.franceterme_id = franceterme_id;
        return this;
    }

    public String getCategory () {
        return category;
    }

    public RelatedTerm setCategory (String category) {
        this.category = category;
        return this;
    }

    public String getStatus () {
        return status;
    }

    public RelatedTerm setStatus (String status) {
        this.status = status;
        return this;
    }


    public String getLangage () {
        return langage;
    }

    public RelatedTerm setLangage (String langage) {
        this.langage = langage;
        return this;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append("title:" + title + ", cat:" + category + ", id:" + franceterme_id + ", status:" + status);
        return builder.toString();
    }


}
