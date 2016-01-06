package cn.goodjobs.common.activity.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class UpdateUserInfoActivity extends BaseActivity {

    SearchItemView itemUserName, itemPassword, itemMobile;
    Button btnLogout;
    boolean isUpdate; // 是否修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_update_user_info;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("登录信息修改");
        itemUserName = (SearchItemView) findViewById(R.id.itemUserName);
        itemPassword = (SearchItemView) findViewById(R.id.itemPassword);
        itemMobile = (SearchItemView) findViewById(R.id.itemMobile);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        itemUserName.setOnClickListener(this);
        itemPassword.setOnClickListener(this);
        itemMobile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemUserName) {
            intent.setClass(this, UpdateUserNameActivity.class);
        } else if (v.getId() == R.id.itemPassword) {
            intent.setClass(this, UpdatePasswordActivity.class);
        } else if (v.getId() == R.id.itemMobile) {
            intent.setClass(this, UpdateMobileActivity.class);
        } else if (v.getId() == R.id.btnLogout) {
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_USER_LOGOUT, this);
            return;
        }
        startActivityForResult(intent, 111);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        SharedPrefUtil.saveDataToLoacl("isLogin", false);
        TipsUtil.show(this, "" + data);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("formLogout", true);
        startActivityForResult(intent, 111);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isUpdate = true;
        }
    }

    @Override
    protected void back() {
        if (isUpdate) {
            setResult(RESULT_OK);
        }
        super.back();
    }
}
