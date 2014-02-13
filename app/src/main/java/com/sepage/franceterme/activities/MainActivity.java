package com.sepage.franceterme.activities;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sepage.franceterme.R;
import com.sepage.franceterme.fragments.DiscoverTermsFragment;
import com.sepage.franceterme.fragments.InfoFragment;
import com.sepage.franceterme.fragments.ProposeTermFragment;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static MainActivity mainActivity;
    public static final int TAB_DISCOVER_INDEX = 0, TAB_SUGGEST_INDEX = 1, TAB_INFO_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

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

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Set up the action bar.
        // No icon

        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_search));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_edit));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setIcon(R.drawable.ic_action_about));
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
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public static void selectTab(int tabIndex) {
        mainActivity.mViewPager.setCurrentItem(tabIndex);
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
