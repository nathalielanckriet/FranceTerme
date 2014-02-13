package com.sepage.franceterme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.sepage.franceterme.R;

public class SplashScreen extends ActionBarActivity implements View.OnClickListener{

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
            startMainActivity();
            return;
        }
        getSupportActionBar().hide();
        setContentView(R.layout.splashscreen_layout);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.splash_button) {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        preferences.edit().putBoolean(OPENED_KEY,true).apply();
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
    }


}
