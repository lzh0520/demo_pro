package com.doit.activity.socialutils.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.utils.FixedToastUtils;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.utils.VidStsUtil;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.doit.activity.socialutils.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzh on 2019/4/22.
 */

public class AliVideoActivity extends Activity implements View.OnClickListener {

    private final String TAG = "AliVideoActivity";

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    private List<String> logStrs = new ArrayList<>();

    private TextView mTvBack;
    private TextView mTvTitle;
    private TextView mTvRight;

    private AliyunVodPlayer mAliyunVodPlayer;
    private AliyunVodPlayerView mAliyunVodPlayerView = null;

    private LinearLayout mTop;


//    private String mUrl = "https://apd-8b2790c9eff2ef30c17ad45a19945d5c.v.smtcdns.com/omts.tc.qq.com/ASDZ6R_lWoYs8dGTM3YGIeoYhJX-w7LJPwfQS59WKdCc/uwMROfz0r5zEIaQXGdGnC2df645FZGCV1Z6Pc-ehLZl3B-cm/PCdfO4frnkwNYG9Ugyyw19tJTyt54w_OmjFtB_IldI37tK0mUOdvz3bm2e9BlMjT_c9zMTb915FT5ITml3YWJbDmighx4cP6xKWTH95YLFk95fbJek7imOOr7Y_dAOtx5e0v-6zDkfKg_mo3AHENDpCGEE29TS2B/h0866reo7zk.321002.ts.m3u8?ver=4";
    private String mUrl = "http://player.alicdn.com/video/aliyunmedia.mp4";


    private boolean inRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isStrangePhone()) {
            //            setTheme(R.style.ActTheme);
        } else {
            setTheme(com.aliyun.vodplayer.R.style.NoActionTheme);
        }

        setContentView(R.layout.activity_alivideo);
        init();
    }

    public void init() {
        requestVidSts();
        mAliyunVodPlayer = new AliyunVodPlayer(getApplicationContext());
//
        mTop = findViewById(R.id.top);

        mTvTitle = findViewById(R.id.text_center);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(getString(R.string.text_ali_video));


        mTvBack = findViewById(R.id.text_left);
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setOnClickListener(this);


        mTvRight = findViewById(R.id.text_right);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(this);


        initAliyunPlayerView();


    }

    /**
     * 请求sts
     */
    private void requestVidSts() {

        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener(this));
    }

    private void initAliyunPlayerView() {
        mAliyunVodPlayerView = findViewById(com.aliyun.vodplayer.R.id.video_view);
        //保持屏幕敞亮
        mAliyunVodPlayerView.setKeepScreenOn(true);
        PlayParameter.PLAY_PARAM_URL = mUrl;
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        //mAliyunVodPlayerView.setCirclePlay(true);
        mAliyunVodPlayerView.setAutoPlay(true);
//        mAliyunVodPlayerView.setOnRePlayListener(new MyRePlayListener(this));
        mAliyunVodPlayerView.setOnPreparedListener(new MyPrepareListener(this));
        mAliyunVodPlayerView.setNetConnectedListener(new MyNetConnectedListener(this));
        mAliyunVodPlayerView.setOnCompletionListener(new MyCompletionListener(this));
//        mAliyunVodPlayerView.setOnFirstFrameStartListener(new MyFrameInfoListener(this));
        mAliyunVodPlayerView.setOnChangeQualityListener(new MyChangeQualityListener(this));
//        mAliyunVodPlayerView.setOnStoppedListener(new MyStoppedListener(this));
//        mAliyunVodPlayerView.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
//        mAliyunVodPlayerView.setOrientationChangeListener(new MyOrientationChangeListener(this));
//        mAliyunVodPlayerView.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
        mAliyunVodPlayerView.setOnTimeExpiredErrorListener(new MyOnTimeExpiredErrorListener(this));
//        mAliyunVodPlayerView.setOnShowMoreClickListener(new MyShowMoreClickLisener(this));
        mAliyunVodPlayerView.setOnRePlayListener(new MyRePlayListener());
//        mAliyunVodPlayerView.setOnPlayStateBtnClickListener(new MyPlayStateBtnClickListener(this));
//        mAliyunVodPlayerView.setOnSeekCompleteListener(new MySeekCompleteListener(this));
//        mAliyunVodPlayerView.setOnSeekStartListener(new MySeekStartListener(this));
        mAliyunVodPlayerView.enableNativeLog();


        videoPlay();

//        changePlayVidSource(PlayParameter.PLAY_PARAM_VID, "播放");
    }

    /**
     * 播放URL视频
     */
    public void videoPlay() {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(PlayParameter.PLAY_PARAM_URL);
        Uri uri = Uri.parse(PlayParameter.PLAY_PARAM_URL);
        if ("rtmp".equals(uri.getScheme())) {
            alsb.setTitle("");
        }
        AliyunLocalSource localSource = alsb.build();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.setLocalSource(localSource);
        }


    }

    /**
     * 切换播放vid资源
     *
     * @param vid   切换视频的vid
     * @param title 切换视频的title
     */
    private void changePlayVidSource(String vid, String title) {
        AliyunVidSts vidSts = new AliyunVidSts();
        PlayParameter.PLAY_PARAM_VID = vid;
        vidSts.setVid(PlayParameter.PLAY_PARAM_VID);
        vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
        vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
        vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);
        vidSts.setTitle(title);
        mAliyunVodPlayerView.setVidSts(vidSts);
    }


    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            mTop.setVisibility(View.VISIBLE);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }

                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTop.setVisibility(View.GONE);
                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

    protected boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
//                || "odin".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        updatePlayerViewMode();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }


    public static class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<AliVideoActivity> activityWeakReference;

        public MyPrepareListener(AliVideoActivity skinActivity) {
            activityWeakReference = new WeakReference(skinActivity);
        }

        @Override
        public void onPrepared() {
            AliVideoActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onPrepared();
            }
        }
    }

    private void onPrepared() {
        logStrs.add(format.format(new Date()) + getString(com.aliyun.vodplayer.R.string.log_prepare_success));

        for (String log : logStrs) {
//            tvLogs.append(log + "\n");
        }
        //视频播放准备成功
        FixedToastUtils.show(AliVideoActivity.this.getApplicationContext(), com.aliyun.vodplayer.R.string.toast_prepare_success);
    }


    public class MyRePlayListener implements IAliyunVodPlayer.OnRePlayListener {


        @Override
        public void onReplaySuccess() {

            mAliyunVodPlayer.replay();

        }
    }


    /**
     * 判断是否有网络的监听
     */
    public class MyNetConnectedListener implements AliyunVodPlayerView.NetConnectedListener {
        WeakReference<AliVideoActivity> weakReference;

        public MyNetConnectedListener(AliVideoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            AliVideoActivity activity = weakReference.get();
            if (activity != null) {
//                activity.onReNetConnected(isReconnect);
            }
        }

        @Override
        public void onNetUnConnected() {
            AliVideoActivity activity = weakReference.get();
            if (activity != null) {
//                activity.onNetUnConnected();
            }
        }
    }

//    private void onNetUnConnected() {
//        currentError = ErrorInfo.UnConnectInternet;
//        if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
//            downloadManager.stopDownloadMedias(aliyunDownloadMediaInfoList);
//        }
//    }

//    private void onReNetConnected(boolean isReconnect) {
//        currentError = ErrorInfo.Normal;
//        if (isReconnect) {
//            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
//                int unCompleteDownload = 0;
//                for (AliyunDownloadMediaInfo info : aliyunDownloadMediaInfoList) {
//                    if (info.getStatus() == AliyunDownloadMediaInfo.Status.Stop) {
//                        unCompleteDownload++;
//                    }
//                }
//
//                if (unCompleteDownload > 0) {
//                    FixedToastUtils.show(this, "网络恢复, 请手动开启下载任务...");
//                }
//            }
//            // 如果当前播放列表为空, 网络重连后需要重新请求sts和播放列表, 其他情况不需要
//            if (alivcVideoInfos != null && alivcVideoInfos.size() == 0) {
//                VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new AliyunPlayerSkinActivity.MyStsListener(this));
//            }
//        }
//    }


    public static class MyChangeQualityListener implements IAliyunVodPlayer.OnChangeQualityListener {

        private WeakReference<AliVideoActivity> activityWeakReference;

        public MyChangeQualityListener(AliVideoActivity skinActivity) {
            activityWeakReference = new WeakReference(skinActivity);
        }

        @Override
        public void onChangeQualitySuccess(String finalQuality) {

            AliVideoActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualitySuccess(finalQuality);
            }
        }

        @Override
        public void onChangeQualityFail(int code, String msg) {
            AliVideoActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualityFail(code, msg);
            }
        }
    }

    private void onChangeQualitySuccess(String finalQuality) {
        logStrs.add(format.format(new Date()) + getString(com.aliyun.vodplayer.R.string.log_change_quality_success));
        FixedToastUtils.show(AliVideoActivity.this.getApplicationContext(),
                getString(com.aliyun.vodplayer.R.string.log_change_quality_success));
    }

    void onChangeQualityFail(int code, String msg) {
        logStrs.add(format.format(new Date()) + getString(com.aliyun.vodplayer.R.string.log_change_quality_fail) + " : " + msg);
        FixedToastUtils.show(AliVideoActivity.this.getApplicationContext(),
                getString(com.aliyun.vodplayer.R.string.log_change_quality_fail));
    }


    public static class MyOnTimeExpiredErrorListener implements IAliyunVodPlayer.OnTimeExpiredErrorListener {

        WeakReference<AliVideoActivity> weakReference;

        public MyOnTimeExpiredErrorListener(AliVideoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onTimeExpiredError() {
            AliVideoActivity activity = weakReference.get();
            if (activity != null) {
                activity.onTimExpiredError();
            }
        }
    }

    /**
     * 鉴权过期
     */
    public void onTimExpiredError() {
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new RetryExpiredSts(this));
    }

    /**
     * 因为鉴权过期,而去重新鉴权
     */
    private static class RetryExpiredSts implements VidStsUtil.OnStsResultListener {

        private WeakReference<AliVideoActivity> weakReference;

        public RetryExpiredSts(AliVideoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(String vid, String akid, String akSecret, String token) {
            AliVideoActivity activity = weakReference.get();
            if (activity != null) {
                activity.onStsRetrySuccess(vid, akid, akSecret, token);
            }
        }

        @Override
        public void onFail() {

        }
    }


    private void onStsRetrySuccess(String mVid, String akid, String akSecret, String token) {
        PlayParameter.PLAY_PARAM_VID = mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        inRequest = false;

        AliyunVidSts vidSts = new AliyunVidSts();
        vidSts.setVid(PlayParameter.PLAY_PARAM_VID);
        vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
        vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
        vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);

        mAliyunVodPlayerView.setVidSts(vidSts);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }
//
//        if (downloadManager != null && downloadDataProvider != null) {
//            downloadManager.stopDownloadMedias(downloadDataProvider.getAllDownloadMediaInfo());
//        }

    }


    public class MyCompletionListener implements IAliyunVodPlayer.OnCompletionListener {

        private WeakReference<AliVideoActivity> activityWeakReference;

        public MyCompletionListener(AliVideoActivity skinActivity) {
            activityWeakReference = new WeakReference(skinActivity);
        }

        @Override
        public void onCompletion() {
           // changePlayVidSource(PlayParameter.PLAY_PARAM_VID, "播放");

            videoPlay();
        }
    }

    private static class MyStsListener implements VidStsUtil.OnStsResultListener {

        private WeakReference<AliVideoActivity> weakctivity;

        MyStsListener(AliVideoActivity act) {
            weakctivity = new WeakReference(act);
        }

        @Override
        public void onSuccess(String vid, final String akid, final String akSecret, final String token) {
            AliVideoActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsSuccess(vid, akid, akSecret, token);
            }
        }

        @Override
        public void onFail() {
            AliVideoActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsFail();
            }
        }
    }

    private void onStsFail() {

        FixedToastUtils.show(getApplicationContext(), com.aliyun.vodplayer.R.string.request_vidsts_fail);
        inRequest = false;
        //finish();
    }

    private void onStsSuccess(String mVid, String akid, String akSecret, String token) {
        PlayParameter.PLAY_PARAM_VID = mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        inRequest = false;

        // 视频列表数据为0时, 加载列表
//        if (alivcVideoInfos != null && alivcVideoInfos.size() == 0) {
//            alivcVideoInfos.clear();
//            loadPlayList();
//        }
    }


    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }

//        if (playerHandler != null) {
//            playerHandler.removeMessages(DOWNLOAD_ERROR);
//            playerHandler = null;
//        }
//
//        if (commenUtils != null) {
//            commenUtils.onDestroy();
//            commenUtils = null;
//        }
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.text_left:

                finish();

                break;
            case R.id.text_right:


                break;


        }

    }

}
