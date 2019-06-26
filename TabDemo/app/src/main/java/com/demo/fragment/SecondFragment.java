package com.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tab.test.com.tabdemo.R;

/**
 * Created by lzh on 2017/10/20.
 */

public class SecondFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_second, null);


        return mView;
    }

    public static SecondFragment newInstance(String content) {
        SecondFragment lf = new SecondFragment();
        final Bundle args = new Bundle();
        args.putString("content", content);
        lf.setArguments(args);
        return lf;
    }

}
