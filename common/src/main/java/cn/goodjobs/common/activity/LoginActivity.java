package cn.goodjobs.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;

import cn.goodjobs.common.AndroidBUSBean;
import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.personalcenter.RegisterActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.CryptUtils;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

public class LoginActivity extends BaseActivity {

    EditText etUser;
    EditText etPassword;
    ImageButton btnClearUser, btnClearPwd;
    CheckBox checkbox;
    ProgressBar progressBar;
    RelativeLayout rlLogin;
    LoginInfo loginInfo;
    TextView textView, btnRegister;

    public static int LOGIN_REQUEST_CODE = 911;
    private String tag;
    boolean formLogout; // 来源于退出登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWeightClick() {
        etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(s.toString())) {
                    btnClearUser.setVisibility(View.INVISIBLE);
                } else {
                    btnClearUser.setVisibility(View.VISIBLE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(s.toString())) {
                    btnClearPwd.setVisibility(View.INVISIBLE);
                } else {
                    btnClearPwd.setVisibility(View.VISIBLE);
                }
            }
        });
        btnClearUser.setOnClickListener(this);
        btnClearPwd.setOnClickListener(this);
        rlLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    protected void initWeight() {
        setTopTitle("登录");
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnClearUser = (ImageButton) findViewById(R.id.btnClearUser);
        btnClearPwd = (ImageButton) findViewById(R.id.btnClearPwd);
        textView = (TextView) findViewById(R.id.textview);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        formLogout = getIntent().getBooleanExtra("formLogout", false);

        if (SharedPrefUtil.getObject("loginInfo") != null) {
            // 表示已经登录过了
            loginInfo = (LoginInfo) SharedPrefUtil.getObject("loginInfo");
            etUser.setText(loginInfo.userName);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnClearUser) {
            etUser.setText("");
        } else if (v.getId() == R.id.btnClearPwd) {
            etPassword.setText("");
        } else if (v.getId() == R.id.rlLogin) {
            if (validate()) {
                login();
            }
        } else if (v.getId() == R.id.btnRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("正在登录...");
        rlLogin.setEnabled(false);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", etUser.getText().toString().trim());
        params.put("password", CryptUtils.Base64Encode(CryptUtils.RSAEncode(etPassword.getText().toString().trim())));
        params.put("isremember", checkbox.isChecked() ? 2 : 0);
        HttpUtil.post(URLS.API_PERSON_LOGIN, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);

        JSONObject jsonObject = (JSONObject) data;
        loginInfo = new LoginInfo(etUser.getText().toString().trim(), etPassword.getText().toString().trim(),
                checkbox.isChecked(), System.currentTimeMillis(), jsonObject.optString("userUserID"));
        textView.setText("登录");
        rlLogin.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        SharedPrefUtil.saveObjectToLoacl("loginInfo", loginInfo);
        SharedPrefUtil.saveDataToLoacl("isLogin", true);
        if (formLogout) {
            String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
            Intent intent = new Intent();
            if (Constant.module.ApplyJobs.toString().equals(defaultModule)) {
                // 全职
                intent.setClassName(this, "cn.goodjobs.applyjobs.activity.ApplyJobsActivity");
            } else if (Constant.module.Lanling.toString().equals(defaultModule)) {
                // 蓝领
                intent.setClassName(this, "cn.goodjobs.bluecollar.activity.BlueCollarActivity");
            }
            intent.putExtra("pageIndex", 3); // 返回到个人中心
            startActivity(intent);
            finish();
            return;
        } else {
            setResult(RESULT_OK);
        }
        if (URLS.JOB_search_login.equals(this.tag)) {
            AndroidBUSBean androidBUSBean = new AndroidBUSBean(AndroidBUSBean.STATUSREFRESH);
            EventBus.getDefault().post(androidBUSBean, URLS.JOB_search_login);
        }
        finish();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        textView.setText("登录");
        rlLogin.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        if (errorCode == 6) {
            // 已登录
            loginInfo = new LoginInfo(etUser.getText().toString().trim(), etPassword.getText().toString().trim(),
                    checkbox.isChecked(), System.currentTimeMillis(), "");
            SharedPrefUtil.saveObjectToLoacl("loginInfo", loginInfo);
            SharedPrefUtil.saveDataToLoacl("isLogin", true);
            if (formLogout) {
                String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
                Intent intent = new Intent();
                if (Constant.module.ApplyJobs.toString().equals(defaultModule)) {
                    // 全职
                    intent.setClassName(this, "cn.goodjobs.applyjobs.activity.ApplyJobsActivity");
                } else if (Constant.module.Lanling.toString().equals(defaultModule)) {
                    // 蓝领
                    intent.setClassName(this, "cn.goodjobs.bluecollar.activity.BlueCollarActivity");
                }
                intent.putExtra("pageIndex", 3); // 返回到个人中心
                startActivity(intent);
                finish();
                return;
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        textView.setText("登录");
        rlLogin.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 验证输入
     */
    public boolean validate() {
        if (StringUtil.isEmpty(etUser.getText().toString().trim())) {
            TipsUtil.show(this, "请输入用户名");
            return false;
        }
        if (StringUtil.isEmpty(etPassword.getText().toString().trim())) {
            TipsUtil.show(this, "请输入密码");
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");
    }

    @Override
    protected void back() {
        if (formLogout) {
            // 登录直接返回到改模块的首页
            String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
            Intent intent = new Intent();
            if (Constant.module.ApplyJobs.toString().equals(defaultModule)) {
                // 全职
                intent.setClassName(this, "cn.goodjobs.applyjobs.activity.ApplyJobsActivity");
            } else if (Constant.module.Lanling.toString().equals(defaultModule)) {
                // 蓝领
                intent.setClassName(this, "cn.goodjobs.bluecollar.activity.BlueCollarActivity");
            }
            startActivity(intent);
        } else {
            super.back();
        }
    }
}
