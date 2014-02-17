package com.sepage.franceterme.view.activities;

import java.util.Locale;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sepage.franceterme.R;
import com.sepage.franceterme.data.DataPool;
import com.sepage.franceterme.view.fragments.DiscoverTermsFragment;
import com.sepage.franceterme.view.fragments.InfoFragment;
import com.sepage.franceterme.view.fragments.ProposeTermFragment;
import com.sepage.franceterme.view.util.ViewUtil;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static MainActivity mainActivity;
    private ImageButton search_bttn1, edit_bttn2, about_bttn3;
    public static final int TAB_DISCOVER_INDEX = 0, TAB_SUGGEST_INDEX = 1, TAB_INFO_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        if (!getIntent().getBooleanExtra(SplashScreen.DATA_IS_INITIALIZED,false)) {     // if data hasnt been setup, do it now.
            DataPool.initializeAppData(this);
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        ((View)findViewById(android.R.id.home).getParent()).setVisibility(View.GONE);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.actionbar_custom);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ((TextView) findViewById(R.id.customActionBar_banner)).setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-LightItalic.ttf"));

        // for swiping
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });


        search_bttn1 = (ImageButton) findViewById(R.id.customActionBar_button1_search);
        edit_bttn2 = (ImageButton) findViewById(R.id.customActionBar_button2_edit);
        about_bttn3 = (ImageButton) findViewById(R.id.customActionBar_button3_about);

        // Set up the action bar.
        // No icon

        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_search));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_edit));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_about));

        actionBar.hide();
        manageButtons(0);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        manageButtons(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public static void selectTab(int tabIndex) {
        Log.d("Tab", "Chose tab: "+tabIndex);
        mainActivity.mViewPager.setCurrentItem(tabIndex);
    }


    @Override
    public void finalize() throws Throwable {
        if (DataPool.getDatabaseHelper()!= null) {
            DataPool.getDatabaseHelper().close();
        }
    }

    private void manageButtons(int selectedIndex) {
        Log.d("Tab", "managing buttons: "+selectedIndex);
        search_bttn1.setSelected(false);
        edit_bttn2.setSelected(false);
        about_bttn3.setSelected(false);
        switch (selectedIndex) {
            case 0: {
                search_bttn1.setSelected(true);
                break;
            }
            case 1: {
                edit_bttn2.setSelected(true);
                break;
            }
            case 2: {
                about_bttn3.setSelected(true);
                break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customActionBar_button1_search: {
                selectTab(0);
                break;
            }
            case R.id.customActionBar_button2_edit: {
                selectTab(1);
                break;
            }
            case R.id.customActionBar_button3_about: {
                selectTab(2);
                break;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Tab", "Chose tab: "+position);
            switch (position) {
                case 0: {
                    return new DiscoverTermsFragment();
                }
                case 1: {
                    return new ProposeTermFragment();
                }
                case 2: {
                    return new InfoFragment();
                }
                default: {
                    return new DiscoverTermsFragment();
                }
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.home_tab1_title).toUpperCase(l);
                case 1:
                    return getString(R.string.home_tab2_title).toUpperCase(l);
                case 2:
                    return getString(R.string.home_tab3_title).toUpperCase(l);
            }
            return null;
        }
    }
}
