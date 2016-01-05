package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;

public class UpdateMobileActivity extends BaseActivity {

    InputItemView itemOldMobile,itemVerCode;
    TextView btnVerCode;
    int min = 60; // 60秒后获取验证码
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_update_mobile;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("修改手机号");
        itemOldMobile = (InputItemView) findViewById(R.id.itemOldMobile);
        itemVerCode = (InputItemView) findViewById(R.id.itemVerCode);
        btnVerCode = (TextView) findViewById(R.id.btnVerCode);
        itemOldMobile.setEditGravityLeft();
        itemVerCode.setEditGravityLeft();
        itemOldMobile.setText(GoodJobsApp.getInstance().personalInfo.optString("mb"));
    }

    @Override
    protected void initData() {

    }

    public void getVerCode(View view) {
        if (itemOldMobile.isEmpty()) {
            return;
        }
        if (!StringUtil.isPhone(itemOldMobile.getText())) {
            TipsUtil.show(this, "您输入的手机号码格式不正确");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", itemOldMobile.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_MOBILECHECK, params, this);
    }

    public void doUpdate(View view) {
        if (itemOldMobile.isEmpty() || itemVerCode.isEmpty()) {
            return;
        }
        if (!StringUtil.isPhone(itemOldMobile.getText())) {
            TipsUtil.show(this, "您输入的手机号码格式不正确");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", itemOldMobile.getText());
        params.put("verCode", itemVerCode.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_MOBILECHECK1, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_MOBILECHECK)) {
            TipsUtil.show(this, data.toString());
            btnVerCode.setEnabled(false);
            mHandler.post(verCodeRun);
        } else if (tag.equals(URLS.API_USER_MOBILECHECK1)) {
            TipsUtil.show(this, data+"");
            LoginInfo loginInfo = GoodJobsApp.getInstance().getLoginInfo();
            loginInfo.userName = itemOldMobile.getText();
            SharedPrefUtil.saveObjectToLoacl("loginInfo", loginInfo);
            setResult(RESULT_OK);
            finish();
        }
    }

    private Runnable verCodeRun = new Runnable() {
        @Override
        public void run() {
            if (min > 0) {
                btnVerCode.setText((min--)+"秒后重新获取");
                mHandler.postDelayed(this, 1000);
            } else {
                min = 60;
                btnVerCode.setText("获取验证码");
                btnVerCode.setEnabled(true);
            }
        }
    };
}
