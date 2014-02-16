/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.data.db.adapters;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.data.db.DatabaseHelper;
import com.sepage.franceterme.data.db.util.SQLUtil;

import java.util.HashMap;

// functions of this class: get HashMap<id,title> of all terms
public class SQLAdapter {

    private static DatabaseHelper database;
    private static final int ID_COLUMN=0, TITLE_COLUMN=1;

    public static HashMap<String, String> getAllIDsAndTitlesFromDatabase (Context context) {
        Log.d("SQL Cache", "Setting up id-title hashmap");
        database = DataPool.getDatabaseHelper();
        Cursor query = database.executeRawQuery(SQLUtil.SELECT_IDS_TITLES, null);
        HashMap<String,String> results = new HashMap<String, String>();

        for(int i=0; i<query.getCount(); i++) {
            query.moveToNext();
            results.put(query.getString(ID_COLUMN), query.getString(TITLE_COLUMN));
        }
        return results;
    }

}
