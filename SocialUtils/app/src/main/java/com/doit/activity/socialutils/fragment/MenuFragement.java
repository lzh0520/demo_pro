package com.doit.activity.socialutils.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.base64.Base64;
import com.doit.activity.socialutils.view.WebImageView;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzh on 2018/4/19.
 * 上传图片：https://blog.csdn.net/it666dhw/article/details/78502892
 */

public class MenuFragement extends Fragment implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {


    private View mView;
    public static final String mUrl = "http://appapi.doit.cn/index.php?adtype=1&a=topimg&c=Index&m=Home";
    public static final String mUrl1 = "http://appapi.doit.cn/index.php?";
    private WebImageView mImg;
    private WebImageView mImg1;

    private WebImageView mImgPdf;
    private static final String FILENAME = "notice.pdf";
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;


    private PDFView pdfView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_menu, null);

        initView();


        postInfo();
        readPdf();
        showPdf();

//        getInfo();

        return mView;
    }

    public void initView() {

        mImg = (WebImageView) mView.findViewById(R.id.imgview);
        mImg1 = (WebImageView) mView.findViewById(R.id.imgview1);
        mImgPdf = (WebImageView) mView.findViewById(R.id.img_pdf);
        pdfView = (PDFView) mView.findViewById(R.id.pdfView);

    }


    public void postInfo() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("adtype", "1")
                .add("a", "topimg")
                .add("c", "Index")
                .add("m", "Home")
                .build();

        final Request request = new Request.Builder()
                .url(mUrl1)
                .post(body).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("data", "错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("data", response.body().string());

                if (response.isSuccessful()) {

                    String param = response.body().string();

                    try {
                        final JSONArray content = new JSONArray(param);

                        if (content.length() > 0) {

                            for (int i = 0; i < content.length(); i++) {
                                JSONObject js = content.getJSONObject(i);


                                final String title = js.getString("title");


                            }

                        }
                        JSONObject js1 = content.getJSONObject(0);
                        final String picture = js1.getString("picture");
                        Log.i("data", picture);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImg.setImageWithURL(getActivity(), picture);
                                try {
                                    mImg1.setRoundImageWithURL(getActivity(), content.getJSONObject(2).getString("picture"), R.mipmap.ic_launcher, 20, true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void getInfo() {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(mUrl).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e("data", Thread.currentThread().getName() + "结果  " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                Log.e("data", Thread.currentThread().getName() + "结果  " + response.body().string());
            }
        });

    }


    /**
     * pdf文件查看
     */
    public void readPdf() {
        Log.i("data", "readpdf");
        File file = new File(getActivity().getCacheDir(), FILENAME);
        Log.i("data", "file=========" + file.exists());

        if (file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.

            try {
                InputStream asset = getActivity().getAssets().open(FILENAME);

                Log.i("data", "asset=========" + asset);

                FileOutputStream output = new FileOutputStream(file);
                final byte[] buffer = new byte[1024];
                int size;
                while ((size = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                asset.close();
                output.close();


                mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

                Log.i("data", "ParcelFileDescriptor=========" + mFileDescriptor);
                // This is the PdfRenderer we use to render the PDF.
                if (mFileDescriptor != null) {
                    mPdfRenderer = new PdfRenderer(mFileDescriptor);
                }


                Bitmap bitmap = Bitmap.createBitmap(480, 480,
                        Bitmap.Config.ARGB_8888);


                Log.i("data", "bitmap=========" + bitmap + "img====" + getBase64ToBitmap(bitmap));
//                mImgPdf.setImageBitmap(bitmap);


                mImgPdf.setImageWithURL(getActivity(), getBase64ToBitmap(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    public static String getBase64ToBitmap(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bo);
            byte[] cpsed = bo.toByteArray();
            byte[] ot = Base64.encodeBase64(cpsed);
            return new String(ot);
        }
        return null;
    }


    public void showPdf() {

        pdfView.fromAsset("h5.pdf")   //设置pdf文件地址
                .defaultPage(1)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(false)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻页
//                 .pages()  //把 5 过滤掉
                .load();


    }


    @Override
    public void onPageChanged(int page, int pageCount) {
//        ToastUtils.showShort(getActivity(), "current" + page);
    }

    @Override
    public void loadComplete(int nbPages) {
//        ToastUtils.showShort(getActivity(), "load complete");
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }
}
