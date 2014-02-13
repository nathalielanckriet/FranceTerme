/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


public class Subdomain {

    public String title;
    public String sqlid;

    public Subdomain(){}

    public Subdomain (String title, String sqlid) {
        this.title = title;
        this.sqlid=sqlid;
    }

    public String getTitle () {
        return title;
    }

    public Subdomain setTitle (String title) {
        this.title = title;
        return this;
    }

    public String getSqlid() {
        return sqlid;
    }

    public Subdomain setSqlid(String sqlid) {
        this.sqlid = sqlid;
        return this;
    }

    public String toString () {
       return title;
    }
}
