package cn.goodjobs.common.baseclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.http.HttpResponseHandler;

/**
 * Created by 王刚 on 2015/12/15 0015.
 */
public class BaseFragment extends Fragment implements HttpResponseHandler, View.OnClickListener {

    protected boolean isLoad; // 界面数据是否加载

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void setTopTitle(View view, String title) {
        TextView tvToptitle = (TextView) view.findViewById(R.id.top_title);
        tvToptitle.setText(title);
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

    @Override
    public void onClick(View v) {

    }
}
