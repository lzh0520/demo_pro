package com.doit.activity.socialutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.just.agentweb.AgentWeb;
import com.doit.activity.socialutils.R;

/**
 * Created by lzh on 2018/5/18.
 */

public class ContentActivity extends Activity implements View.OnClickListener {

    private TextView mTextBack;
    private TextView mTextTitle;

    private LinearLayout mLinearWeb;
    private AgentWeb mAgent;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadinfo);

        initView();
        loadContent();

    }

    public void initView() {

        mUrl = getIntent().getStringExtra("news_url");

        mTextBack = (TextView) findViewById(R.id.text_left);
        mTextBack.setVisibility(View.VISIBLE);
        mTextBack.setOnClickListener(this);

        mTextTitle = (TextView) findViewById(R.id.text_center);
        mTextTitle.setVisibility(View.VISIBLE);
        mTextTitle.setText("详情");


        mLinearWeb = (LinearLayout) findViewById(R.id.linear_content);


    }


    public void loadContent() {

        Log.i("data",mUrl);
        mAgent = AgentWeb.with(this).setAgentWebParent
                (mLinearWeb, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .createAgentWeb()
                .ready()
                .go(mUrl);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.text_left:

                finish();

                break;

        }

    }
}
