package com.doit.activity.socialutils.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.just.agentweb.AgentWeb;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.activity.LifeApplication;
import com.doit.activity.socialutils.activity.MainActivity;
import com.doit.activity.socialutils.interfaces.OnPictureIntentResultListener;
import com.doit.activity.socialutils.util.AppConst;
import com.doit.activity.socialutils.util.ToastUtils;
import com.doit.activity.socialutils.util.Utils;
import com.doit.activity.socialutils.view.CircleImageView;
import com.doit.activity.socialutils.view.WebImageView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import okhttp3.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzh on 2018/4/19.
 */

public class PersonalFragement extends Fragment implements View.OnClickListener {


    private View mView;

    private CircleImageView mImgHead;
    private WebImageView mImgPic;
    private Button mBtnUpload;

    private Activity mContext;

    private Dialog bottomDialog;

    private TextView mTVLogin;


    public final static int TAKE_PHOTO_WITH_DATA = 2023;
    public final static int CAMERA_WITH_DATA = 2024;
    public static final int PHOTO_RESOULT = 2025;
    public static final int SELECT_RESOULT = 2026;
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private OnPictureIntentResultListener picListener;
    private Bitmap userUploadBitmap;

    private String Url = "https://ehuiapi.ehub.net/ecapi/api/ehuimanager/modifyPersonalInformation";
    String fileprovider = null;

    private LinearLayout mLinearWeb;
    private AgentWeb mAgent;
    private String mLoadUrl = "http://yhapp.djlearn.cnpc.com.cn/vdjcloud/newWeb/index.html?eventId=217&userId=89";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_mine, null);

        initView();

        return mView;
    }

    /**
     * initView
     */

    public void initView() {

        mContext = getActivity();

        mImgHead = mView.findViewById(R.id.img_Head);
//        mImgHead.setOnClickListener(this);

        mImgPic = mView.findViewById(R.id.img_picture);
        mImgPic.setOnClickListener(this);

        mBtnUpload = mView.findViewById(R.id.btn_upload);
        mBtnUpload.setOnClickListener(this);

        mLinearWeb = mView.findViewById(R.id.linear_webview);

        mTVLogin = mView.findViewById(R.id.text_wxLgoin);
        mTVLogin.setOnClickListener(this);



        loadWeb();


        registToWX();
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        LifeApplication.mWxApi = WXAPIFactory.createWXAPI(getActivity(), AppConst.APP_ID, false);
        // 将该app注册到微信
        LifeApplication.mWxApi.registerApp(AppConst.APP_ID);
    }


    /**
     * 加载网页
     */
    public void loadWeb() {

        mAgent = AgentWeb.with(getActivity()).setAgentWebParent
                (mLinearWeb, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .createAgentWeb()
                .ready()
                .go(mLoadUrl);

    }


    /**
     * 选择图片View
     */
    public void chosePic() {


        MainActivity acti = (MainActivity) mContext;
        if (acti.isActive) {
            bottomDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);

            View view = View.inflate(mContext, R.layout.dialog_camara, null);
            bottomDialog.setContentView(view);

            Window window = bottomDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);

            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.y = 10;

            Display display = mContext.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            attributes.width = width;

            window.setAttributes(attributes);

            TextView takePhoto = (TextView) view.findViewById(R.id.me_take_photo);
            TextView localPhoto = (TextView) view.findViewById(R.id.me_local_photo);
            TextView cancell = (TextView) view.findViewById(R.id.me_cancell);

            takePhoto.setOnClickListener(this);
            localPhoto.setOnClickListener(this);
            cancell.setOnClickListener(this);

            bottomDialog.setCancelable(true);

            bottomDialog.show();

        } else {
            ToastUtils.showShort(mContext, "请重试");
        }


    }

    /**
     * 拍照
     */
    public void takePhone() {


        try {
            fileprovider = Utils.getPackageName(mContext) + ".fileprovider";
            Log.i("data", "---------" + fileprovider);
            File file = new File(mContext.getExternalCacheDir(), "img.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(mContext, fileprovider, file));

//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        FileProvider.getUriForFile(this, Constant.CAMERA_PACKAGE_NAME, file));


            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            startActivityForResult(intent, CAMERA_WITH_DATA);


        } catch (Exception e) {

            ToastUtils.showShort(mContext, "相机暂不可用!");

        }


    }


    public void uploadHead() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Log.i("data", "上传------" + userUploadBitmap);

        final RequestBody body = new FormBody.Builder()
                .add("gender", "1")
                .add("companyname", "ehui")
                .add("euserid", "1404")
                .add("position", "java")
                .add("realname", "马哥")
                .add("headimage", Utils.getBase64ToBitmap(userUploadBitmap))
                .build();


        final Request request = new Request.Builder()
                .url(Url)
                .post(body).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("data", "错误信息：" + e.toString() + body);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                Log.i("data", response.body().string());

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {


                            ToastUtils.showLong(mContext, "上传成功!");
                        }
                    }
                });

            }
        });
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("circleCrop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("aspectX", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
//        intent.putExtra("scale",true);//自由截取
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }


    private class PicListener implements OnPictureIntentResultListener {

        @SuppressLint("NewApi")
        @Override
        public void onPictureIntentResult(Bitmap bitmap) {
            userUploadBitmap = bitmap;
            if (null != userUploadBitmap) {
                mImgPic.setImageBitmap(userUploadBitmap);
            }
        }

        @Override
        public void OnException(Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 适配Android N 以上拍照返回结果处理
     */
    public void camResult() {


        try {
            fileprovider = Utils.getPackageName(mContext) + ".fileprovider";
            //设置文件保存路径这里放在跟目录下
            File picture = new File(mContext.getExternalCacheDir() + "/img.jpg");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(mContext, fileprovider, picture);
                //裁剪照片
                startPhotoZoom(uri);
            } else {
                //裁剪照片
                startPhotoZoom(Uri.fromFile(picture));
            }
        } catch (Exception e) {

        }

    }


    public boolean startPicture(OnPictureIntentResultListener listener) {

        this.picListener = listener;
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType(IMAGE_UNSPECIFIED);
            this.startActivityForResult(intent, TAKE_PHOTO_WITH_DATA);
        } catch (ActivityNotFoundException e) {

            System.gc();
            this.picListener = null;

            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != mContext.RESULT_OK)
            return;

        switch (requestCode) {
            case TAKE_PHOTO_WITH_DATA:

                if (null == data) {
                    return;
                }
                Uri originalUri = data.getData();
                if (null == originalUri) {
                    return;
                }
                //
                String picPath = null;
                File picFile = null;
                Bitmap dynamicBitmap = null;
                String scheme = originalUri.getScheme();
                if (null == scheme || scheme.length() < 1) {
                    return;
                }
                if (scheme.equalsIgnoreCase("file")) {
                    picPath = originalUri.getPath();
                    picFile = new File(picPath);
                    dynamicBitmap = Utils.decodeFile(picFile);
                } else if (scheme.equalsIgnoreCase("content")) {
                    Cursor cursor = mContext.getContentResolver().query(originalUri, null,
                            null, null, null);
                    cursor.moveToFirst();
                    picPath = cursor.getString(1);
                    picFile = new File(picPath);
                    dynamicBitmap = Utils.decodeFile(picFile);
                }
                this.picListener.onPictureIntentResult(dynamicBitmap);


                break;
            case CAMERA_WITH_DATA:
                camResult();
                break;
            case PHOTO_RESOULT:

                Bundle extras = data.getExtras();
                File file = new File(mContext.getExternalCacheDir() + "/head.jpg");
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        photo.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    userUploadBitmap = photo;
                    mImgPic.setImageBitmap(userUploadBitmap);
                }

                break;

        }

    }



    @Override
    public void onClick(View view) {

        PicListener picListener = new PicListener();

        switch (view.getId()) {


            case R.id.img_picture:

                chosePic();

                break;

            case R.id.me_cancell:

                bottomDialog.dismiss();

                break;

            case R.id.me_take_photo:

                bottomDialog.dismiss();
                takePhone();

                break;

            case R.id.me_local_photo:

                bottomDialog.dismiss();
                startPicture(picListener);

                break;

            case R.id.btn_upload:

                if (userUploadBitmap != null) {

                    uploadHead();

                } else {
                    ToastUtils.showShort(mContext, "上传失败");
                }
                break;

            case R.id.text_wxLgoin:

                if (!LifeApplication.mWxApi.isWXAppInstalled()) {
                    ToastUtils.showShort(getActivity(), "您还未安装微信客户端");
                    return;
                }
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "diandi_wx_login";
                LifeApplication.mWxApi.sendReq(req);


                break;


        }

    }


}
