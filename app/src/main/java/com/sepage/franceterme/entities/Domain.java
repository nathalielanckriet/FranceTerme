/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


public class Domain {


    public String title;
    public String sqlid;

    public Domain() { }

    public Domain (String title, String sqlid) {
        this.title = title;
        this.sqlid=sqlid;
    }

    public String getTitle () {
        return title;
    }

    public Domain setTitle (String title) {
        this.title = title;
        return this;
    }

    public Domain setSqlid(String sqlid) {
        this.sqlid = sqlid;
        return this;
    }

    public String getSqlid() {
        return sqlid;
    }

    public String toString() {
        return title;
    }
}
