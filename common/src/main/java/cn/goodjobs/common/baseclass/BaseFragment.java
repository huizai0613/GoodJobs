package cn.goodjobs.common.baseclass;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.WebViewActivity;
import cn.goodjobs.common.adapter.MainBanaerAdapter;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by 王刚 on 2015/12/15 0015.
 */
public class BaseFragment extends Fragment implements HttpResponseHandler, View.OnClickListener {

    public boolean isLoad; // 界面数据是否加载
    protected AutoScrollViewPager adViewPager;
    protected double adScale = 4;
    int scrollTime = 3000; // 广告3秒滚动一屏
    protected FragmentActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onFailure(int statusCode, String tag) {

    }

    @Override
    public void onSuccess(String tag, Object data) {

    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {

    }

    @Override
    public void onProgress(String tag, int progress) {

    }


    @Override
    public void onDetach() {
        HttpUtil.cancelAllRequests();
        super.onDetach();
    }

    protected void setTopTitle(View view, String title) {
        TextView tvToptitle = (TextView) view.findViewById(R.id.top_title);
        tvToptitle.setText(title);
        tvToptitle.setVisibility(View.VISIBLE);
    }

    // 设置副标题
    protected void setTopSubTitle(View view, String subTitle) {
        TextView tvTopSubTitle = (TextView) view.findViewById(R.id.top_sub_title);
        tvTopSubTitle.setText(subTitle);
        tvTopSubTitle.setVisibility(View.VISIBLE);
    }

    // 隐藏返回按钮
    protected void hideBackBtn(View view) {
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_left);
        backBtn.setVisibility(View.INVISIBLE);
    }

    //改变左侧图片
    protected void changeLeftBg(View view, int id) {
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_left);
        backBtn.setImageResource(id);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) getResources().getDimension(R.dimen.padding_small), 0, 0, 0);
        backBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        backBtn.setLayoutParams(params);
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化顶部广告图
     */
    protected void initAd(JSONArray jsonArray) {
        // 广告图片
        if (jsonArray != null && jsonArray.length() > 0) {
            int len = jsonArray.length();
            List<SimpleDraweeView> imageList = new ArrayList<SimpleDraweeView>();
            if (len == 2) {
                len = 4;
            }
            for (int i = 0; i < len; ++i) {
                final JSONObject jsonObject = jsonArray.optJSONObject(i % jsonArray.length());
                if (jsonObject.has("width")) {
                    adScale = jsonObject.optDouble("width") / jsonObject.optDouble("height");
                }
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(mActivity).inflate(R.layout.simpledraweeview, null);
                String imageUrl = jsonObject.optString("image");
                Uri uri = Uri.parse(imageUrl);
                if (imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF")) {
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setAutoPlayAnimations(true)
                            .setUri(uri)//设置uri
                            .build();
                    simpleDraweeView.setController(draweeController);
                } else {
                    simpleDraweeView.setImageURI(uri);
                }
                simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("title", jsonObject.optString("title"));
                        intent.putExtra("url", jsonObject.optString("url"));
                        startActivity(intent);
                    }
                });
                imageList.add(simpleDraweeView);
            }
            final MainBanaerAdapter mainBanaerAdapter = new MainBanaerAdapter(getActivity(), imageList);
            adViewPager.setAdapter(mainBanaerAdapter);
            adViewPager.setInterval(scrollTime);
            adViewPager.setCycle(true);
            adViewPager.startAutoScroll(scrollTime);
            adViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);

            LinearLayout.LayoutParams layoutParams
                    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (HttpUtil.getDisplayMetrics().widthPixels / adScale));
            adViewPager.setLayoutParams(layoutParams);
            adViewPager.setVisibility(View.VISIBLE);
        } else {
            adViewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adViewPager != null && adViewPager.getVisibility() == View.VISIBLE) {
            adViewPager.startAutoScroll(scrollTime);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adViewPager != null && adViewPager.getVisibility() == View.VISIBLE) {
            adViewPager.stopAutoScroll();
        }
    }

    protected void runMainThread(Runnable runnable) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(runnable);
        }
    }
}
