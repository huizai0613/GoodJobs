package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.CryptUtils;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.VerCode;
import cn.goodjobs.common.view.searchItem.InputItemView;

public class RegisterActivity extends BaseActivity {

    InputItemView itemOldMobile, itemImgCode, itemVerCode, itemPassword;
    TextView btnVerCode;
    ImageView imgCode;
    int min = 60; // 60秒后获取验证码
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("注册");
        itemOldMobile = (InputItemView) findViewById(R.id.itemOldMobile);
        itemImgCode = (InputItemView) findViewById(R.id.itemImgCode);
        itemVerCode = (InputItemView) findViewById(R.id.itemVerCode);
        itemPassword = (InputItemView) findViewById(R.id.itemPassword);
        imgCode = (ImageView) findViewById(R.id.imgCode);
        btnVerCode = (TextView) findViewById(R.id.btnVerCode);
        itemOldMobile.setEditGravityLeft();
        itemImgCode.setEditGravityLeft();
        itemVerCode.setEditGravityLeft();
        itemPassword.setEditGravityLeft();

        changImageCode();
    }

    public void changeVerCode(View view) {
        changImageCode();
    }

    // 变换图形验证码
    private void changImageCode() {
        imgCode.setImageBitmap(VerCode.getInstance().getBitmap());
    }

    public void getVerCode(View view) {
        if (itemOldMobile.isEmpty()) {
            TipsUtil.show(this, "请输入手机号码");
            return;
        }
        if (itemImgCode.isEmpty()) {
            TipsUtil.show(this, "请输入图形验证码");
            return;
        }
        if (!StringUtil.isPhone(itemOldMobile.getText())) {
            TipsUtil.show(this, "您输入的手机号码格式不正确");
            return;
        }
        if (!itemImgCode.getText().toLowerCase().equals(VerCode.getInstance().getCode().toLowerCase())) {
            TipsUtil.show(this, "您的图形验证码输入有误");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", itemOldMobile.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_REGISTERSMS, params, this);
    }

    public void doRegister(View view) {
        if (itemOldMobile.isEmpty() || itemVerCode.isEmpty() || itemPassword.isEmpty()) {
            return;
        }
        if (!StringUtil.isPhone(itemOldMobile.getText())) {
            TipsUtil.show(this, "您输入的手机号码格式不正确");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", itemOldMobile.getText());
        params.put("passwd", CryptUtils.Base64Encode(CryptUtils.RSAEncode(itemPassword.getText().toString().trim())));
        params.put("smsCodeStr", itemVerCode.getText());
        params.put("ut", 8);
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_REGISTERNEW, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_REGISTERSMS)) {
            TipsUtil.show(this, data.toString());
            btnVerCode.setEnabled(false);
            mHandler.post(verCodeRun);
        } else if (tag.equals(URLS.API_USER_REGISTERNEW)) {
            TipsUtil.show(this, data + "");
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
                btnVerCode.setText((min--) + "秒后重新获取");
                mHandler.postDelayed(this, 1000);
            } else {
                min = 60;
                btnVerCode.setText("获取验证码");
                btnVerCode.setEnabled(true);
            }
        }
    };

    @Override
    protected void initData() {

    }

}
