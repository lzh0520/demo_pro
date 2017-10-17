package audio.test.com.audiodemo.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import audio.test.com.audiodemo.R;
import audio.test.com.audiodemo.util.ToastUtils;


/**
 * Created by lzh on 2017/9/28.
 */

public class MAdapter extends BaseAdapter {

    private List<String> mData;
    private Context mContext;
    private MyHolder myHolder;

    private MediaPlayer mPlayer = null;// 播放器
    private String playFileName;

    private boolean isPausePlay = false;

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/LocalAudio";

    private AudioListener mListener;


    public MAdapter(Context context, List<String> list) {

        this.mContext = context;
        this.mData = list;


    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

//        if (view == null) {
        myHolder = new MyHolder();

        mListener = new AudioListener(i);

        view = LayoutInflater.from(mContext).inflate(R.layout.audio_item, null);

        myHolder.name = (TextView) view.findViewById(R.id.text_name);
        myHolder.play = (TextView) view.findViewById(R.id.text_play);

        // view.setTag(myHolder);

//        } else {

        //  myHolder = (MyHolder) view.getTag();
//        }

        myHolder.name.setText(mData.get(i));
        myHolder.play.setOnClickListener(mListener);

        return view;
    }


    // 播放录音
    private void playRecord(String fileName) {


        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();

            if (isPausePlay) {
                myHolder.play.setBackgroundResource(R.mipmap.desktop_pause);
                isPausePlay = false;


                mPlayer.start();
            } else {
                if (mPlayer != null) {
                    mPlayer.pause();
                }
                myHolder.play.setBackgroundResource(R.mipmap.desktop_play);
                isPausePlay = true;
            }


        } catch (Exception e) {

            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
            ToastUtils.showShort(mContext, "播放错误" + playFileName);

        }

        // 播放完毕的监听
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完毕改变状态，释放资源
                mPlayer.release();
                mPlayer = null;

            }
        });
    }


    private class AudioListener implements View.OnClickListener {

        // 点击列表位置

        private int position;

        public AudioListener(int position) {

            this.position = position;

        }

        @Override
        public void onClick(View v) {
            playFileName = path + "/" + mData.get(position);

            playRecord(playFileName);

        }
    }


    class MyHolder {

        TextView name;
        TextView play;

    }
}
