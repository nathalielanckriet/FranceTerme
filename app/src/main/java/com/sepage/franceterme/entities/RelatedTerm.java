/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.entities;

public class RelatedTerm {

    public String sqlid, title, franceterme_id,
            category,   // n.m., etc
            status,     // antonyme, synonyme
            langage;


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
