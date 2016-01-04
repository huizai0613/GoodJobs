package cn.goodjobs.common.baseclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.GoodJobsSettingActivity;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by 王刚 on 2015/12/14 0014.
 * activity的基类
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener, HttpResponseHandler
{
    protected BaseActivity mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
        mcontext = this;
        setContentView(getLayoutID());
        initData();
        initWeight();
        initWeightClick();
    }

    /**
     * 设置布局资源
     *
     * @return
     */
    protected abstract int getLayoutID();

    /**
     * 设置控件的事件
     */
    protected abstract void initWeightClick();

    /**
     * 初始化控件
     */
    protected abstract void initWeight();

    /**
     * 获取数据
     */
    protected abstract void initData();


    @Override
    public void onClick(View v)
    {

    }

    @Override
    public void onFailure(int statusCode, String tag)
    {

    }

    @Override
    public void onSuccess(String tag, Object data)
    {

    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage)
    {

    }

    @Override
    public void onProgress(String tag, int progress)
    {

    }

    /**
     * 退出当前界面
     */
    public void doBack(View view)
    {
        back();
    }

    /**
     * 跳转到系统设置界面
     */
    public void toSetting(View view) {
        Intent intent = new Intent(this, GoodJobsSettingActivity.class);
        startActivity(intent);
    }

    protected void back()
    {
        HttpUtil.cancelRequest(); // 返回上级界面前取消当前界面的网络请求
        hideSoftInputFromWindow();
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }

    @Override
    protected void onDestroy()
    {
        ScreenManager.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    protected void setTopTitle(String title)
    {
        TextView tvToptitle = (TextView) findViewById(R.id.top_title);
        tvToptitle.setVisibility(View.VISIBLE);
        tvToptitle.setText(title);
    }

    // 设置副标题
    protected void setTopSubTitle(String subTitle)
    {
        TextView tvTopSubTitle = (TextView) findViewById(R.id.top_sub_title);
        tvTopSubTitle.setText(subTitle);
        tvTopSubTitle.setVisibility(View.VISIBLE);
    }


    protected void hideSoftInputFromWindow()
    {
        View currentFocus = getWindow().getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    currentFocus.getWindowToken(), 0);
        }


    }


    protected boolean checkJsonError(JSONObject jsonObject)
    {

        int errorCode = jsonObject.optInt("errorCode");

        if (errorCode != 0) {
            TipsUtil.show(mcontext, jsonObject.optString("errorMessage"));
            return true;
        }
        return false;
    }

}
