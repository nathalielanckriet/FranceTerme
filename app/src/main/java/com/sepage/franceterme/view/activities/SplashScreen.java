package com.sepage.franceterme.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    public static final String OPENED_KEY = "APP_ALREADY_OPENED", DATA_IS_INITIALIZED="DATA_IS_INITIALIZED";
    private SharedPreferences preferences;

    public SplashScreen() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);
        if (preferences.getBoolean(OPENED_KEY, false)) {    // if its not the first time the app is opened
            startMainActivity(false);
        }
        else {      // a bit of time to setup database!
            DataPool.initializeAppData(this);
        }

        getSupportActionBar().hide();
        setContentView(R.layout.splashscreen_layout);
    }


    private void startMainActivity(boolean dataIsInitialized) { // if there isn't time to initialize databases, leave it for the next activity
        preferences.edit().putBoolean(OPENED_KEY, true).apply();
        startActivity(new Intent(SplashScreen.this, MainActivity.class).putExtra(DATA_IS_INITIALIZED, dataIsInitialized));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.splash_button) {
            startMainActivity(true);
        }
    }


}
