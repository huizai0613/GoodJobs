package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.activity.personalcenter.ResumeOpenSettingActivity;
import cn.goodjobs.common.activity.personalcenter.UpdateMobileActivity;
import cn.goodjobs.common.activity.personalcenter.UpdatePasswordActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

/**
 * Created by zhuli on 2016/1/7.
 */
public class ItemSettingActivity extends BaseActivity {

    SearchItemView itemPassword, itemMobile, itemPublic;
    Button btnLogout;
    boolean isUpdate; // 是否修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("设置");
        itemPublic = (SearchItemView) findViewById(R.id.itemPublic);
        itemPassword = (SearchItemView) findViewById(R.id.itemPassword);
        itemMobile = (SearchItemView) findViewById(R.id.itemMobile);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        itemPassword.setOnClickListener(this);
        itemPublic.setOnClickListener(this);
        itemMobile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        if (!GoodJobsApp.getInstance().isLogin()) {
            btnLogout.setVisibility(View.INVISIBLE);
        } else {
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemPassword) {
            intent.setClass(this, UpdatePasswordActivity.class);
        } else if (v.getId() == R.id.itemMobile) {
            intent.setClass(this, UpdateMobileActivity.class);
        } else if (v.getId() == R.id.btnLogout) {
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_USER_LOGOUT, this);
            return;
        } else if (v.getId() == R.id.itemPublic) {
            intent.setClass(this, ResumeOpenSettingActivity.class);
        }
        startActivityForResult(intent, 111);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        SharedPrefUtil.saveDataToLoacl("isLogin", false);
        TipsUtil.show(this, "" + data);
        Intent intent = new Intent(this, LoginActivity.class);
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
