package cn.goodjobs.common.activity.resume;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

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

public class MyResumeWorkAddActivity extends BaseActivity {

    SearchItemView itemStarttime, itemEndtime, itemJobDes;
    SelectorItemView itemIndtype, itemCorpkind, itemJobfunc;
    InputItemView itemCompanyName, itemDepart;
    Button btnSave;
    private String expID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_work_add;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("工作经历");
        itemStarttime = (SearchItemView) findViewById(R.id.itemStarttime);
        itemEndtime = (SearchItemView) findViewById(R.id.itemEndtime);
        itemJobDes = (SearchItemView) findViewById(R.id.itemJobDes);
        itemIndtype = (SelectorItemView) findViewById(R.id.itemIndtype);
        itemCorpkind = (SelectorItemView) findViewById(R.id.itemCorpkind);
        itemJobfunc = (SelectorItemView) findViewById(R.id.itemJobfunc);
        itemCompanyName = (InputItemView) findViewById(R.id.itemCompanyName);
        itemDepart = (InputItemView) findViewById(R.id.itemDepart);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        itemJobDes.setOnClickListener(this);
        expID = getIntent().getStringExtra("expID");
        if (StringUtil.isEmpty(expID)) {
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", "");
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", "");
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("expID", expID);
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_EDIT_WEXP, params, this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_SAVE_WEXP)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_CV_EDIT_WEXP)) {
            JSONObject jsonObject = (JSONObject) data;
            itemStarttime.setText(jsonObject.optString("fMonth"));
            itemEndtime.setText(jsonObject.optString("tMonth"));
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", jsonObject.optString("fMonth"));
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", jsonObject.optString("tMonth"));

            itemJobDes.setText(jsonObject.optString("jobDescription"));
            itemCompanyName.setText(jsonObject.optString("company"));
            itemJobfunc.setText(jsonObject.optString("jobCNName"));
            itemDepart.setText(jsonObject.optString("department"));
            itemIndtype.setText(jsonObject.optString("industryName"));
            itemCorpkind.setText(jsonObject.optString("comTypeName"));
            itemIndtype.setSelectorIds(jsonObject.optString("industry"));
            itemCorpkind.setSelectorIds(jsonObject.optString("comType"));
            itemJobfunc.setSelectorIds(jsonObject.optString("jobName"));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSave) {
            doSave();
        } else if (v.getId() == R.id.itemJobDes) {
            Intent intent = new Intent(this, TextAreaActivity.class);
            intent.putExtra("title", "工作描述");
            intent.putExtra("content", itemJobDes.getText());
            startActivityForResult(intent, 111);
        }
    }

    private void doSave() {
        if (itemStarttime.isEmpty() || itemEndtime.isEmpty() || itemJobDes.isEmpty() || itemCompanyName.isEmpty() ||
                itemJobfunc.isEmpty() || itemDepart.isEmpty() || itemIndtype.isEmpty() || itemCorpkind.isEmpty()) {
            return;
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(expID)) {
                params.put("expID", expID);
            }
            params.put("fMonth", itemStarttime.getText() + "-01");
            params.put("tMonth", itemEndtime.getText() + "-01");
            params.put("company", itemCompanyName.getText());
            params.put("industry", itemIndtype.getSelectorIds());
            params.put("companyType", itemCorpkind.getSelectorIds());
            params.put("jobName", itemJobfunc.getSelectorIds());
            params.put("department", itemDepart.getText());
            params.put("jobDescription", itemJobDes.getText());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_SAVE_WEXP, params, this);
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
