/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
 =- Copyright (c) 2014. Sépage SAS
 =- @author: @danakianfar
 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.sepage.franceterme.entities;


import com.sepage.franceterme.data.db.util.SQLHelper;
import com.sepage.franceterme.data.db.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;

public class Domain implements SQLHelper<Domain> {


    private String title;
    private String sqlid;

    public Domain() { }

    public Domain (String title, String sqlid) {
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
        return SQLUtil.getSQLInsertCommand(DOMAIN_TABLE, columns,values);
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
    public boolean equals(Domain object) {
        return title.equals(object.getTitle());
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
