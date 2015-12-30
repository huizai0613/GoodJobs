package cn.goodjobs.common.activity.resume;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

public class MyResumeWillActivity extends BaseActivity {

    SelectorItemView itemJobFunc, itemWorkAddress, itemSalary, itemIndtype, itemCheckIn;
    RadioGroup radioGroup;
    JSONObject houseJson0;
    JSONObject houseJson1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_will;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("求职意向");
        itemJobFunc = (SelectorItemView) findViewById(R.id.itemJobFunc);
        itemWorkAddress = (SelectorItemView) findViewById(R.id.itemWorkAddress);
        itemSalary = (SelectorItemView) findViewById(R.id.itemSalary);
        itemIndtype = (SelectorItemView) findViewById(R.id.itemIndtype);
        itemCheckIn = (SelectorItemView) findViewById(R.id.itemCheckIn);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        JSONArray houseJson = (JSONArray) JsonMetaUtil.getObject("house");
        houseJson0 = houseJson.optJSONObject(0);
        houseJson1 = houseJson.optJSONObject(1);
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_WILL, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_WILL)) {
            JSONObject jsonObject = (JSONObject) data;
            itemJobFunc.setSelectorIds(jsonObject.optString("position"));
            itemJobFunc.setText(jsonObject.optString("fmtPosition"));
            itemWorkAddress.setSelectorIds(jsonObject.optString("workPlace"));
            itemWorkAddress.setText(jsonObject.optString("fmtWorkPlace"));
            itemSalary.setSelectorIds(jsonObject.optString("salary"));
            itemIndtype.setSelectorIds(jsonObject.optString("industry"));
            itemIndtype.setText(jsonObject.optString("fmtIndustry"));
            itemCheckIn.setSelectorIds(jsonObject.optString("checkInTime"));
            if (houseJson0.optString("id").equals(jsonObject.optString("house"))) {
                radioGroup.check(R.id.radio0);
            } else {
                radioGroup.check(R.id.radio1);
            }
        } else if (tag.equals(URLS.API_CV_SAVE_WILL)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        }
    }

    public void doSave(View view) {
        if (itemJobFunc.isEmpty() || itemWorkAddress.isEmpty()) {
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("sIndustry", itemIndtype.getSelectorIds());
        params.put("sFunction", itemJobFunc.getSelectorIds());
        params.put("sWorkPlace", itemWorkAddress.getSelectorIds());
        params.put("salary", itemSalary.getSelectorIds());
        params.put("ckTime", itemCheckIn.getSelectorIds());
        if (radioGroup.getCheckedRadioButtonId() == R.id.radio0) {
            params.put("house", houseJson0.optString("id"));
        } else {
            params.put("house", houseJson1.optString("id"));
        }
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_SAVE_WILL, params, this);
    }
}
