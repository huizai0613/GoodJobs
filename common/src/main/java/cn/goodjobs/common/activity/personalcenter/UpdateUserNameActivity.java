package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;

public class UpdateUserNameActivity extends BaseActivity {

    InputItemView itemUserName, itemNewUserName, itemPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_update_user_name;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("修改用户名");
        itemUserName = (InputItemView) findViewById(R.id.itemUserName);
        itemNewUserName = (InputItemView) findViewById(R.id.itemNewUserName);
        itemPass = (InputItemView) findViewById(R.id.itemPass);

        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_UserChangename, this);
    }

    @Override
    protected void initData() {

    }

    public void doUpdate(View view) {
        if (itemNewUserName.isEmpty() || itemPass.isEmpty()) {
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberName", itemNewUserName.getText());
        params.put("password", itemPass.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_USERNAMEEDIT, URLS.API_USER_USERNAMEEDIT, params, this, 10030);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_USERNAMEEDIT)) {
            TipsUtil.show(this, ((JSONObject) data).optString("message"));
            LoginInfo loginInfo = GoodJobsApp.getInstance().getLoginInfo();
            loginInfo.userName = itemNewUserName.getText();
            SharedPrefUtil.saveObjectToLoacl("loginInfo", loginInfo);
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_USER_UserChangename)) {
            itemUserName.setHint(((JSONObject) data).optString("memberName"));
            itemUserName.setEditable(false);
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (errorCode == 10030) {
            AlertDialogUtil.show(this, errorMessage, "员名称只能包含英文字母、阿拉伯数字、下划线、实点及@符合，长度不能为零。");
        }
    }
}
