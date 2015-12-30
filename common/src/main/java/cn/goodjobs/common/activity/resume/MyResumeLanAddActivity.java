package cn.goodjobs.common.activity.resume;

import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
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

public class MyResumeLanAddActivity extends BaseActivity {

    SelectorItemView itemLanguage, itemLanguageLevel;
    private String lanID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_lan_add;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("语言能力");
        itemLanguage = (SelectorItemView) findViewById(R.id.itemLanguage);
        itemLanguageLevel = (SelectorItemView) findViewById(R.id.itemLanguageLevel);
        lanID = getIntent().getStringExtra("lanID");
        if (!StringUtil.isEmpty(lanID)) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("lanID", lanID);
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_EDIT_LAN, params, this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_SAVE_LAN)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_CV_EDIT_LAN)) {
            JSONObject jsonObject = (JSONObject) data;
            itemLanguage.setText(jsonObject.optString("languageName"));
            itemLanguageLevel.setText(jsonObject.optString("levelName"));
            itemLanguage.setSelectorIds(jsonObject.optString("language"));
            itemLanguageLevel.setSelectorIds(jsonObject.optString("level"));
        }
    }

    public void doSave(View view) {
        if (itemLanguage.isEmpty() || itemLanguageLevel.isEmpty()) {
            return;
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(lanID)) {
                params.put("lanID", lanID);
            }

            params.put("language", itemLanguage.getSelectorIds());
            params.put("level", itemLanguageLevel.getSelectorIds());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_SAVE_LAN, params, this);
        }
    }
}
