package cn.goodjobs.common.activity.resume;

import android.view.View;
import android.widget.Button;

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

public class MyResumeTrainAddActivity extends BaseActivity {

    SearchItemView itemStarttime, itemEndtime;
    InputItemView itemDanwei, itemKecheng, itemZhengshu;
    private String trainID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_train_add;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("培训经历");
        itemStarttime = (SearchItemView) findViewById(R.id.itemStarttime);
        itemEndtime = (SearchItemView) findViewById(R.id.itemEndtime);
        itemDanwei = (InputItemView) findViewById(R.id.itemDanwei);
        itemKecheng = (InputItemView) findViewById(R.id.itemKecheng);
        itemZhengshu = (InputItemView) findViewById(R.id.itemZhengshu);
        trainID = getIntent().getStringExtra("trainID");
        if (StringUtil.isEmpty(trainID)) {
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", "");
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", "");
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("trainID", trainID);
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_EDIT_TRA, params, this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_SAVE_TRA)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        } else if (tag.equals(URLS.API_CV_EDIT_TRA)) {
            JSONObject jsonObject = (JSONObject) data;
            itemStarttime.setText(jsonObject.optString("fMonth"));
            itemEndtime.setText(jsonObject.optString("tMonth"));
            new DatePickerUtil(this, itemStarttime, "yyyy-MM", jsonObject.optString("fMonth"));
            new DatePickerUtil(this, itemEndtime, "yyyy-MM", jsonObject.optString("tMonth"));
            itemDanwei.setText(jsonObject.optString("department"));
            itemKecheng.setText(jsonObject.optString("course"));
            itemZhengshu.setText(jsonObject.optString("cert"));
        }
    }

    public void doSave(View view) {
        if (itemStarttime.isEmpty() || itemDanwei.isEmpty() || itemKecheng.isEmpty() || itemZhengshu.isEmpty()) {
            return;
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(trainID)) {
                params.put("trainID", trainID);
            }
            params.put("fMonth", itemStarttime.getText() + "-01");
            if (!StringUtil.isEmpty(itemEndtime.getText())) {
                params.put("tMonth", itemEndtime.getText() + "-01");
            }
            params.put("department", itemDanwei.getText());
            params.put("course", itemKecheng.getText());
            params.put("cert", itemZhengshu.getText());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_CV_SAVE_TRA, params, this);
        }
    }
}
