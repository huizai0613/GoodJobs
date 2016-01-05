package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;

public class UpdatePasswordActivity extends BaseActivity {
    InputItemView itemOldPass, itemNewPass, itemNewPassAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("修改用户名");
        itemOldPass = (InputItemView) findViewById(R.id.itemOldPass);
        itemNewPass = (InputItemView) findViewById(R.id.itemNewPass);
        itemNewPassAgain = (InputItemView) findViewById(R.id.itemNewPassAgain);
    }

    @Override
    protected void initData() {

    }

    public void doUpdate(View view) {
        if (itemOldPass.isEmpty() || itemNewPass.isEmpty() || itemNewPassAgain.isEmpty()) {
            return;
        }
        if (!itemNewPass.getText().equals(itemNewPassAgain.getText())) {
            TipsUtil.show(this, "您两次输入的密码不一致");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("oldPassword", itemOldPass.getText());
        params.put("newsPassword", itemNewPass.getText());
        params.put("chkPassword", itemNewPassAgain.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_PASSWORDEDIT, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        TipsUtil.show(this, data.toString());
        LoginInfo loginInfo = GoodJobsApp.getInstance().getLoginInfo();
        loginInfo.passWord = itemNewPass.getText();
        SharedPrefUtil.saveObjectToLoacl("loginInfo", loginInfo);
        finish();
    }
}
