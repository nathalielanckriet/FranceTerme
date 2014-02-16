/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.data;


import android.content.Context;

import com.sepage.franceterme.data.db.DatabaseHelper;
import com.sepage.franceterme.entities.Term;

import java.util.HashMap;

public class DataPool {

    private static HashMap<String, String> idTitleCache;
    private static Term randomTerm;
    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static void openDatabaseConnectionForAppLaunch(Context context) {
        DataPool.databaseHelper = new DatabaseHelper(context);
    }

    public static HashMap<String, String> getIdTitleCache() {
        return idTitleCache;
    }

    public static void retrieveIdTitleCache(HashMap<String, String> idTitleCache) {
        DataPool.idTitleCache = idTitleCache;
    }

    public static Term getRandomTerm() {
        return randomTerm;
    }

    public static void setRandomTerm(Term randomTerm) {
        DataPool.randomTerm = randomTerm;
    }
}
