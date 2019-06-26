package com.doit.activity.socialutils.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.fragment.DisFragement;
import com.doit.activity.socialutils.fragment.HomepageFragement;
import com.doit.activity.socialutils.fragment.MineFragement;
import com.doit.activity.socialutils.util.Utils;

public class MainActivity extends AppCompatActivity {


    private Fragment mContent;

    public boolean isActive = true;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:

                    switchContent(new HomepageFragement(), true);


                    return true;

                case R.id.navigation_dashboard:


                    switchContent(new DisFragement(), true);

                    return true;

                case R.id.navigation_notifications:


                    switchContent(new MineFragement(), true);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setTopBar(this);


//        getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, Utils.getNavigationBarHeight(this));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (mContent == null) {
//            mContent = new HomepageFragement();
            mContent = new MineFragement();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mContent).commit();

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    public void switchContent(Fragment fragment, boolean Stack) {
        mContent = fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

}
