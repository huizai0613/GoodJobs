package cn.goodjobs.applyjobs.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.UMShareUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cz.msebera.android.httpclient.Consts;

/**
 * Created by zhuli on 2015/12/18.
 * 资讯详情
 */
public class JobNewsDetailsActivity extends BaseActivity {

    private TextView tv_tips, tv_title, tv_content;
    private ImageButton iv_right;
    private String type;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                tv_content.setText((CharSequence) msg.obj);
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_jobnews_details;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("详情");
        iv_right = (ImageButton) findViewById(R.id.btn_right);
        iv_right.setImageResource(R.mipmap.share);
        iv_right.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_details_title);
        tv_content = (TextView) findViewById(R.id.tv_details_content);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("newsid", 0);
        type = intent.getStringExtra("type");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsID", id);
        HttpUtil.post(URLS.API_JOB_NewsDetails, params, this);
        LoadingDialog.showDialog(this);
    }


    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        final JSONObject object = (JSONObject) data;
        tv_title.setText(object.optString("newstitle"));
        if (type.equals("html")) {
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            //加载html中的图片
            Thread t = new Thread(new Runnable() {
                Message msg = Message.obtain();

                @Override
                public void run() {
                    Html.ImageGetter imageGetter = new Html.ImageGetter() {

                        @Override
                        public Drawable getDrawable(String source) {
                            URL url;
                            Drawable drawable = null;
                            try {
                                url = new URL(source);
                                drawable = Drawable.createFromStream(
                                        url.openStream(), null);
                                drawable.setBounds(0, 0,
                                        drawable.getMinimumWidth(),
                                        drawable.getMinimumWidth());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return drawable;
                        }
                    };
                    CharSequence test = Html.fromHtml(object.optString("newscontent"), imageGetter, null);
                    msg.what = 1;
                    msg.obj = test;
                    myHandler.sendMessage(msg);
                }
            });
            t.start();
        } else if (type.equals("normal")) {
            tv_content.setText(object.optString("newscontent"));
        }
    }


    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void rightBtnClick(View v) {
        UMShareUtil.setShareText(this, "http://m.goodjobs.cn/index.php/module/Corp/action/NewId?newId=", tv_title.getText().toString(), getIntent().getIntExtra("newsid", 0) + "");
        UMShareUtil.getUMSocialService().openShare(this, false);
    }
}

