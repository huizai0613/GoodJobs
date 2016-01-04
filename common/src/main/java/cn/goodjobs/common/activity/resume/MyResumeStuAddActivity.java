package cn.goodjobs.common.activity.resume;

import android.content.Intent;
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

public class MyResumeStuAddActivity extends BaseActivity {

    SearchItemView itemStarttime, itemEndtime, itemJobDes;
    RadioGroup radioGroup;
    InputItemView itemJobName;
    private String pracID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_stu_add;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("教育经历");
        itemStarttime = (SearchItemView) findViewById(R.id.itemStarttime);
        itemEndtime = (SearchItemView) findViewById(R.id.itemEndtime);
        itemJobDes = (SearchItemView) findViewById(R.id.itemJobDes);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        itemJobName = (InputItemView) findViewById(R.id.itemJobName);
        pracID = getIntent().getStringExtra("pracID");
        if (StringUtil.isEmpty(pracID)) {
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", "");
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", "");
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("pracID", pracID);
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_EDIT_STU, params, this);
        }
        itemJobDes.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_SAVE_STU)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_CV_EDIT_STU)) {
            JSONObject jsonObject = (JSONObject) data;
            itemStarttime.setText(jsonObject.optString("fMonth"));
            itemEndtime.setText(jsonObject.optString("tMonth"));
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", jsonObject.optString("fMonth"));
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", jsonObject.optString("tMonth"));

            if ("职务".equals(jsonObject.optString("expType"))) {
                radioGroup.check(R.id.radio0);
            } else {
                radioGroup.check(R.id.radio1);
            }
            itemJobName.setText(jsonObject.optString("expName"));
            itemJobDes.setText(jsonObject.optString("expDesc"));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.itemJobDes) {
            Intent intent = new Intent(this, TextAreaActivity.class);
            intent.putExtra("title", "职务描述");
            intent.putExtra("content", itemJobDes.getText());
            startActivityForResult(intent, 111);
        }
    }

    public void doSave(View view) {
        if (itemStarttime.isEmpty() || itemJobName.isEmpty() || itemJobDes.isEmpty()) {
            return;
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(pracID)) {
                params.put("pracID", pracID);
            }
            params.put("fMonth", itemStarttime.getText() + "-01");
            if (!StringUtil.isEmpty(itemEndtime.getText())) {
                params.put("tMonth", itemEndtime.getText() + "-01");
            }
            params.put("expName", itemJobName.getText());
            params.put("expDesc", itemJobDes.getText());
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio0) {
                params.put("expType", "职务");
            } else {
                params.put("expType", "实践");
            }
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_SAVE_STU, params, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            itemJobDes.setText(data.getStringExtra("content"));
        }
    }
}
