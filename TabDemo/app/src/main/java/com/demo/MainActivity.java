package com.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.demo.fragment.FirstFragment;
import com.demo.fragment.SecondFragment;
import com.demo.fragment.ThridFragment;

import tab.test.com.tabdemo.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar navbar;


    private FirstFragment mFmFirst;
    private SecondFragment mFmSecond;
    private ThridFragment mFmThrid;


    private static int MODE = BottomNavigationBar.MODE_DEFAULT;
    private static int STYLE = BottomNavigationBar.BACKGROUND_STYLE_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initNavBar();

    }

    private void initView() {
        navbar = (BottomNavigationBar) findViewById(R.id.bottom_navbar);

        setDefaultFragment();

    }


    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mFmFirst = FirstFragment.newInstance("first");
        transaction.replace(R.id.fragment_content, mFmFirst);
        transaction.commit();
    }

    /**
     * 初始化 BottomNavigationBar
     */
    private void initNavBar() {
        navbar.clearAll();
        //生成BottomNavigationBar中的每一个item
        BottomNavigationItem item1 = new BottomNavigationItem(R.mipmap.ic_home, getString(R.string.fragment_first));
        BottomNavigationItem item2 = new BottomNavigationItem(R.mipmap.ic_chat, getString(R.string.fragment_second));
        BottomNavigationItem item3 = new BottomNavigationItem(R.mipmap.ic_user, getString(R.string.fragment_three));
        //将item添加到BottomNavigationBar中
        navbar.addItem(item1);
        navbar.addItem(item2);
        navbar.addItem(item3);
        //设置BottomNavigationBar的模式
        navbar.setMode(MODE);
        //设置背景模式
        navbar.setBackgroundStyle(STYLE);
        //默认选中项
        navbar.setFirstSelectedPosition(0);
        //统一设置点击颜色
        navbar.setActiveColor(R.color.colorAccent);
        //统一设置未点击颜色
        navbar.setInActiveColor(R.color.colorWhite);
        //统一设置BottomNavigationBar的背景色
        navbar.setBarBackgroundColor(R.color.colorBlue);
        //生成BottomNavigationBar
        navbar.initialise();
        navbar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:

                        if (mFmFirst == null) {
                            mFmFirst = FirstFragment.newInstance("first");
                        }
                        transaction.replace(R.id.fragment_content, mFmFirst);

                        break;
                    case 1:
                        if (mFmSecond == null) {
                            mFmSecond = SecondFragment.newInstance("second");
                        }
                        transaction.replace(R.id.fragment_content, mFmSecond);
                        break;
                    case 2:
                        if (mFmThrid == null) {
                            mFmThrid = ThridFragment.newInstance("three");
                        }
                        transaction.replace(R.id.fragment_content, mFmThrid);
                        break;
                    default:
                        break;
                }
                // 事务提交
                transaction.commit();


            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }


    public void switchFragment(Fragment mFragment, String content) {


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (mFragment == null) {
            mFragment = FirstFragment.newInstance(content);
        }

        transaction.replace(R.id.fragment_content, mFragment);

        // 事务提交
        transaction.commit();
    }

}
