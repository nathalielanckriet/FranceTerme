package com.sepage.franceterme.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.sepage.franceterme.R;
import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.data.db.adapters.SQLAdapter;
import com.sepage.franceterme.data.db.adapters.TermAdapter;

import java.util.Random;

public class SplashScreen extends ActionBarActivity implements View.OnClickListener {

    private final int SPLASHSCREEN_DISPLAY_DURATION = 2000;
    private final String OPENED_KEY = "APP_ALREADY_OPENED";
    private SharedPreferences preferences;

    public SplashScreen() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);

        // looks for OPENED_KEY and returns true if it doesnt exist
        if (preferences.getBoolean(OPENED_KEY, false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, SPLASHSCREEN_DISPLAY_DURATION);
        }

        Log.d("Database Initialization", "Calling database setup from SplashScreen");
        DataPool.openDatabaseConnectionForAppLaunch(this);       // opens the DatabaseHelper class. The oncreate method will trigger the database setup.

        Log.d("Database Initialization", "Checking if database exists from SplashScreen");
        boolean databaseExists = DataPool.getDatabaseHelper().setupDatabase();   // initializes the database if it hasnt been created already
        if (databaseExists) {
            initializeIdTitleCache(this);
            setupRandomTerm();
        }

        getSupportActionBar().hide();
        setContentView(R.layout.splashscreen_layout);
    }


    private void startMainActivity() {
        preferences.edit().putBoolean(OPENED_KEY, true).apply();
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
    }


    public void initializeIdTitleCache(Context appContext) {
        final Context context = appContext;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataPool.retrieveIdTitleCache(SQLAdapter.getAllIDsAndTitlesFromDatabase(context));
            }
        }).run();
    }

    public void setupRandomTerm() {
        int randomIndex = new Random().nextInt(DataPool.getIdTitleCache().size());
        DataPool.setRandomTerm(TermAdapter.getTermFromDatabaseByID(this, Integer.toString(randomIndex)));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.splash_button) {
            startMainActivity();
        }
    }


}
