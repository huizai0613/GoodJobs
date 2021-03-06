package cn.goodjobs.common.activity.resume;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.TextAreaActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
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
        itemKeyword.setOnClickListener(this);

        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_BASIC, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_BASIC)) {
            setDataToView((JSONObject) data);
        } else if (tag.equals(URLS.API_CV_BASICSAVE)) {
            TipsUtil.show(this, ((JSONObject) data).optString("message"));
            setResult(RESULT_OK);
            finish();
        }
    }

    private void setDataToView(JSONObject jsonObject) {
        itemName.setText(jsonObject.optString("realName"));
        itemBirthday.setText(jsonObject.optString("birthday"));
        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", jsonObject.optString("birthday"));
        itemHukou.setText(jsonObject.optString("nCityName"));
        itemHukou.setSelectorIds(jsonObject.optString("nCityID"));
        itemAddress.setText(jsonObject.optString("cityName"));
        itemAddress.setSelectorIds(jsonObject.optString("cityID"));
        itemDegree.setText(jsonObject.optString("fmtDegree"));
        itemDegree.setSelectorIds(jsonObject.optString("degree"));
        itemWorktime.setText(jsonObject.optString("fmtWorktime"));
        itemWorktime.setSelectorIds(jsonObject.optString("worktime"));
        itemSalary.setText(jsonObject.optString("fmtCurrentSalary"));
        itemEmail.setText(jsonObject.optString("email"));
        itemPhone.setHint(jsonObject.optString("mobile"));
        itemPhone.setEditable(false);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.itemKeyword) {
            Intent intent = new Intent(this, TextAreaActivity.class);
            intent.putExtra("title", "关键字");
            intent.putExtra("content", itemKeyword.getText());
            startActivityForResult(intent, 111);
        }
    }

    public void doSave(View view) {
        if (itemName.isEmpty() || itemHukou.isEmpty() || itemAddress.isEmpty()
                || itemDegree.isEmpty() || itemBirthday.isEmpty() || itemWorktime.isEmpty()) {
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("realname", itemName.getText());
        if (sexGroup.getCheckedRadioButtonId() == R.id.radioWuman) {
            params.put("sex", "女");
        } else {
            params.put("sex", "男");
        }
        params.put("realname", itemName.getText());
        params.put("birthday", itemBirthday.getText());
        params.put("nCityID", itemHukou.getSelectorIds());
        params.put("cityID", itemAddress.getSelectorIds());
        params.put("degree", itemDegree.getSelectorIds());
        params.put("worktime", itemWorktime.getSelectorIds());
        params.put("currentSalary", itemSalary.getSelectorIds());
        params.put("email", itemEmail.getText());
        params.put("interests", itemKeyword.getText());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_BASICSAVE, params, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            itemKeyword.setText(data.getStringExtra("content"));
        }
    }
}
