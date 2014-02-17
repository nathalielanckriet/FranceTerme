/*==============================================================================
 = Copyright (c) Sepage SAS, $year.
 = @author: @danakianfar
 =============================================================================*/

package com.sepage.franceterme.data;


import android.content.Context;
import android.util.Log;

import com.sepage.franceterme.data.db.DatabaseHelper;
import com.sepage.franceterme.data.db.adapters.SQLAdapter;
import com.sepage.franceterme.data.db.adapters.TermAdapter;
import com.sepage.franceterme.entities.Term;

import java.util.HashMap;
import java.util.Random;

public class DataPool {

    private static HashMap<String, String> idTitleCache, titleIdCache;
    private static Term randomTerm;
    private static DatabaseHelper databaseHelper;
    private static Context context;

    public static DatabaseHelper getDatabaseHelper() {

        return databaseHelper;
    }

    public static void initializeAppData(Context appContext) {
        context = appContext;
        Log.d("Database Initialization", "Calling database setup from SplashScreen");
        DataPool.openDatabaseConnectionForAppLaunch();       // opens the DatabaseHelper class. The oncreate method will trigger the database setup.

        Log.d("Database Initialization", "Checking if database exists from SplashScreen");
        DataPool.getDatabaseHelper().setupDatabase();   // initializes the database if it hasnt been created already and populates

        initializeIdTitleCache();
        setupRandomTerm();
    }


    public static void openDatabaseConnectionForAppLaunch() {
        DataPool.databaseHelper = new DatabaseHelper(context);
    }

    private static void initializeIdTitleCache() {
        final Context finalContext = context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataPool.setIdTitleCache(SQLAdapter.getIDsAndTitlesFromDatabase(finalContext));
            }
        }).run();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataPool.setTitleIdCache(SQLAdapter.getTitlesAndIDsFromDatabase(finalContext));
            }
        }).run();
    }

    private static void setupRandomTerm() {
        int randomIndex = new Random().nextInt(DataPool.getIDTitleHashMap().size());
        DataPool.setRandomTerm(TermAdapter.getTermFromDatabaseByID(context, Integer.toString(randomIndex)));
    }


    public static HashMap<String, String> getIDTitleHashMap() {
        return idTitleCache;
    }

    public static HashMap<String, String> getTitleIDHashMap() {
        return titleIdCache;
    }

    public static void setIdTitleCache(HashMap<String, String> idTitleCache) {
        DataPool.idTitleCache = idTitleCache;
    }

    public static void setTitleIdCache(HashMap<String, String> titleIdCache) {
        DataPool.titleIdCache = titleIdCache;
    }

    public static void setDatabaseHelper(DatabaseHelper helper) {
        databaseHelper = helper;
    }

    public static Term getRandomTerm() {
        return randomTerm;
    }

    public static void setRandomTerm(Term randomTerm) {
        DataPool.randomTerm = randomTerm;
        Log.d("Random Term", "Random Term setup: " + randomTerm.toString());
    }


}
