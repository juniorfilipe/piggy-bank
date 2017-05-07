package com.example.filipe.piggybank.Views;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.filipe.piggybank.Controller.SectionsPageAdapter;
import com.example.filipe.piggybank.Repository.DatabaseHandler;
import com.example.filipe.piggybank.Views.CoinsFragment;
import com.example.filipe.piggybank.Views.NotesFragment;
import com.example.filipe.piggybank.Views.StatsFragment;
import com.example.filipe.piggybank.Views.TotalFragment;
import com.example.filipe.piggybank.R;
import net.danlew.android.joda.JodaTimeAndroid;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);


        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(new Fragment(),TAG)
                    .commit();
        }
        //joda-time initialization
        JodaTimeAndroid.init(this);
        Log.d(TAG,"onCreate: Starting.");
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (shouldAskPermissions()) {
            askPermissions();
        }


    }

    public void setupViewPager(ViewPager viewPager)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TotalFragment(),"Total");
        adapter.addFragment(new CoinsFragment(),"coins");
        adapter.addFragment(new NotesFragment(),"Notes");
        adapter.addFragment(new StatsFragment(),"Statistics");
        viewPager.setAdapter(adapter);
    }


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
}
