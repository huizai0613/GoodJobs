package cn.goodjobs.common.activity.resume;

import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.util.DateUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

public class MyResumeEduAddActivity extends BaseActivity {

    SearchItemView itemRuxue, itemBiye;
    SelectorItemView itemDegree;
    InputItemView itemSchoolName;
    Button btnSave;
    private String eduID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_edu_add;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("教育经历");
        itemRuxue = (SearchItemView) findViewById(R.id.itemRuxue);
        itemBiye = (SearchItemView) findViewById(R.id.itemBiye);
        itemDegree = (SelectorItemView) findViewById(R.id.itemDegree);
        itemSchoolName = (InputItemView) findViewById(R.id.itemSchoolName);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        eduID = getIntent().getStringExtra("eduID");
        if (StringUtil.isEmpty(eduID)) {
            new DatePickerUtil(this, itemRuxue, "yyyy-MM", "");
            new DatePickerUtil(this, itemBiye, "yyyy-MM", "");
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("eduID", eduID);
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_EDIT_EDU, params, this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_SAVE_EDU)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_CV_EDIT_EDU)) {
            JSONObject jsonObject = (JSONObject) data;
            itemRuxue.setText(jsonObject.optString("fMonth"));
            itemBiye.setText(jsonObject.optString("tMonth"));
            new DatePickerUtil(this, itemRuxue, "yyyy-MM", jsonObject.optString("fMonth"));
            new DatePickerUtil(this, itemBiye, "yyyy-MM", jsonObject.optString("tMonth"));

            itemSchoolName.setText(jsonObject.optString("school"));
            itemDegree.setText(jsonObject.optString("degreeName"));
            itemDegree.setSelectorIds(jsonObject.optString("degree"));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSave) {
            doSave();
        }
    }

    private void doSave() {
        if (itemRuxue.isEmpty() || itemSchoolName.isEmpty() || itemDegree.isEmpty()) {
            return;
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(eduID)) {
                params.put("eduID", eduID);
            }
            if (DateUtil.compare(itemRuxue.getText(), itemBiye.getText()) == 0 || DateUtil.compare(itemRuxue.getText(), itemBiye.getText()) == 1) {
                TipsUtil.show(this, "毕业时间不能小于入学时间");
                return;
            }
            params.put("fMonth", itemRuxue.getText() + "-01");
            if (!StringUtil.isEmpty(itemBiye.getText())) {
                params.put("tMonth", itemBiye.getText() + "-01");
            }
            params.put("school", itemSchoolName.getText());
            params.put("degree", itemDegree.getSelectorIds());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_SAVE_EDU, params, this);
        }
    }
}
