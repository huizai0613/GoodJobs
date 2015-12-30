package cn.goodjobs.common.activity.resume;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

public class MyResumeBaseInfoActivity extends BaseActivity {

    InputItemView itemName, itemPhone, itemEmail;
    SearchItemView itemBirthday, itemKeyword;
    SelectorItemView itemHukou, itemAddress, itemDegree, itemWorktime, itemSalary;
    Button btnSave;
    RadioGroup sexGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_base_info;
    }

    @Override
    protected void initWeightClick() {
    }

    @Override
    protected void initWeight() {
        setTopTitle("个人信息");

        itemName = (InputItemView) findViewById(R.id.itemName);
        itemPhone = (InputItemView) findViewById(R.id.itemPhone);
        itemEmail = (InputItemView) findViewById(R.id.itemEmail);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemKeyword = (SearchItemView) findViewById(R.id.itemKeyword);
        itemHukou = (SelectorItemView) findViewById(R.id.itemHukou);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);
        itemDegree = (SelectorItemView) findViewById(R.id.itemDegree);
        itemWorktime = (SelectorItemView) findViewById(R.id.itemWorktime);
        itemSalary = (SelectorItemView) findViewById(R.id.itemSalary);
        btnSave = (Button) findViewById(R.id.btnSave);
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);

        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_BASIC, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        setDataToView((JSONObject) data);
    }

    private void setDataToView(JSONObject jsonObject) {
        itemName.setText(jsonObject.optString("realName"));
        itemBirthday.setText(jsonObject.optString("birthday"));
        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", jsonObject.optString("birthday"));
        itemHukou.setText(jsonObject.optString("nCityName"));
        itemHukou.setSelectorIds(jsonObject.optString("nCity"));
        if (!StringUtil.isEmpty(jsonObject.optString("netIDName"))) {
            itemAddress.setText(jsonObject.optString("cityName") + " " + jsonObject.optString("netIDName"));
            itemAddress.setSelectorIds(jsonObject.optString("netID"));
        } else if (!StringUtil.isEmpty(jsonObject.optString("cityName"))) {
            itemAddress.setText(jsonObject.optString("cityName"));
            itemAddress.setSelectorIds(jsonObject.optString("city"));
        }
        itemDegree.setText(jsonObject.optString("fmtDegree"));
        itemDegree.setSelectorIds(jsonObject.optString("degree"));
        itemWorktime.setText(jsonObject.optString("fmtWorktime"));
        itemWorktime.setSelectorIds(jsonObject.optString("worktime"));
        itemSalary.setText(jsonObject.optString("fmtCurrentSalary"));
        itemSalary.setSelectorIds(jsonObject.optString("currentSalary"));
        if ("女".equals(jsonObject.optString("sex"))) {
            sexGroup.check(R.id.radioWuman);
        } else {
            sexGroup.check(R.id.radioMan);
        }
        itemKeyword.setText(jsonObject.optString("interests"));
    }

    @Override
    protected void initData() {

    }
}
