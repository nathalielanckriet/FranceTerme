/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


import com.sepage.franceterme.db.util.SQLHelper;
import com.sepage.franceterme.db.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;

public class Subdomain implements SQLHelper {

    public String title;
    public String sqlid;

    public Subdomain(){}

    public Subdomain (String title, String sqlid) {
        this.title = title;
        this.sqlid=sqlid;
    }

    @Override
    public String getInsertQuery() {
        List<String> columns = new ArrayList<String>(), values = new ArrayList<String>();
        if (title!=null) {
            columns.add(TITLE_COLUMN);
            values.add(title);
        }
        return SQLUtil.getSQLInsertCommand(SUBDOMAIN_TABLE, columns, values);
    }

    @Override
    public String getUpdateQuery() {
        return null;
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
