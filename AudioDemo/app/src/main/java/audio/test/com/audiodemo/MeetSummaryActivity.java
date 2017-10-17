package audio.test.com.audiodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import audio.test.com.audiodemo.util.Utils;

/**
 * Created by lzh on 2017/10/16.
 */

public class MeetSummaryActivity extends Activity implements View.OnClickListener {

    private TextView mTextBack;
    private TextView mTextTitle;

    private TextView mTextVoice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ehuilib_meet_summary);

        init();

    }

    public void init() {

        Utils.setWindow(this);

        mTextBack = (TextView) findViewById(R.id.text_top_left);
        mTextBack.setVisibility(View.VISIBLE);
        mTextBack.setBackgroundResource(R.mipmap.ehuilib_back);
        mTextBack.setOnClickListener(this);

        mTextTitle = (TextView) findViewById(R.id.text_top_center);
        mTextTitle.setText(getString(R.string.jiyao_edit));

        mTextVoice = (TextView) findViewById(R.id.text_summary_voice);
        mTextVoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent it = null;

        switch (view.getId()) {


            case R.id.text_top_left:

                finish();

                break;

            case R.id.text_summary_voice:

                it = new Intent(this, VoiceSumActivity.class);
                startActivity(it);

                break;

            default:


                break;

        }
    }
}
