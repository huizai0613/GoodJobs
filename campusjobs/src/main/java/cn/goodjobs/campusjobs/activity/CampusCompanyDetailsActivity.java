package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.activity.LsMapActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.PhoneUtils;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by zhuli on 2015/12/31.
 */
public class CampusCompanyDetailsActivity extends BaseActivity {

    private View jobSimilarBox;
    private TextView comName;
    private TextView comNature;
    private TextView comNum;
    private TextView comIndustry;
    private TextView comAdd;
    private TextView comMap;
    private TextView comPhone;
    private TextView comContent;
    private ImageView comUpdown;
    private int corpID;
    private EmptyLayout error_layout;
    private JSONObject corpData;
    private String loc;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_jobcompany_detail;
    }

    @Override
    protected void initWeightClick() {

        comUpdown.setOnClickListener(this);
        comPhone.setOnClickListener(this);
        comMap.setOnClickListener(this);
        jobSimilarBox.setOnClickListener(this);

    }

    @Override
    protected void initWeight() {
        setTopTitle("公司介绍");
        comName = (TextView) findViewById(R.id.com_name);
        error_layout = (EmptyLayout) findViewById(R.id.error_layout);
        comNature = (TextView) findViewById(R.id.com_nature);
        comNum = (TextView) findViewById(R.id.com_num);
        comIndustry = (TextView) findViewById(R.id.com_industry);
        comAdd = (TextView) findViewById(R.id.com_add);
        comMap = (TextView) findViewById(R.id.com_map);
        comPhone = (TextView) findViewById(R.id.com_phone);
        comContent = (TextView) findViewById(R.id.com_content);
        comUpdown = (ImageView) findViewById(R.id.com_updown);
        jobSimilarBox = findViewById(R.id.job_similar_box);

        Drawable iconPhone = getResources().getDrawable(R.drawable.phone);
        iconPhone.setBounds(0, 0, DensityUtil.dip2px(mcontext, 15), DensityUtil.dip2px(mcontext, 15));
        comPhone.setCompoundDrawables(null, null, iconPhone, null);

        Drawable iconMap = getResources().getDrawable(R.drawable.mapm);
        iconMap.setBounds(0, 0, DensityUtil.dip2px(mcontext, 15), DensityUtil.dip2px(mcontext, 15));
        comMap.setCompoundDrawables(iconMap, null, null, null);

        error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
        HashMap<String, Object> param = new HashMap<>();
        param.put("corpID", corpID);
        HttpUtil.post(URLS.API_JOB_Corpshow, param, this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int curCorpID = intent.getIntExtra("corpID", 0);
        if (this.corpID != curCorpID) {
            error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
            HashMap<String, Object> param = new HashMap<>();
            this.corpID = curCorpID;
            param.put("corpID", curCorpID);
            HttpUtil.post(URLS.API_JOB_Corpshow, param, this);
        }
    }

    @Override
    protected void initData() {
        corpID = getIntent().getIntExtra("corpID", 0);
    }


    private void setData() {
        comName.setText(corpData.optString("corpName"));
        comNature.setText(corpData.optString("corpkind"));
        comNum.setText(corpData.optString("corpsize"));
        comIndustry.setText(corpData.optString("industry"));
        comAdd.setText(corpData.optString("address"));
        String phone = corpData.optString("phone");
        if (StringUtil.isEmpty(phone)) {
            comPhone.setCompoundDrawables(null, null, null, null);
            comPhone.setText("暂无");
            comPhone.setOnClickListener(null);
        } else {
            comPhone.setText(phone + " ");
        }
        comContent.setText(corpData.optString("intro"));

        loc = corpData.optString("loc");
        if (StringUtil.isEmpty(loc)) {
            comMap.setVisibility(View.GONE);
        } else {
            comMap.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        error_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        corpData = (JSONObject) data;
        setData();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        error_layout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int i = v.getId();
        if (i == R.id.com_updown) {
            if (comContent.getMaxLines() < 100) {
                comContent.setMaxLines(100);
                comUpdown.setImageResource(R.drawable.companyarticle_up);
            } else {
                comUpdown.setImageResource(R.drawable.companyarticle_down);
                comContent.setMaxLines(5);
            }
        } else if (i == R.id.com_phone) {
            PhoneUtils.makeCall(comPhone.getText().toString(), mcontext);
        } else if (i == R.id.com_map) {
            LsMapActivity.openMap(mcontext, Double.parseDouble(loc.split(",")[0]), Double.parseDouble(loc.split(",")[1]),
                    corpData.optString("corpName"), corpData.optString("address"));
        } else if (i == R.id.job_similar_box) {//跳转相似职位列表

            HashMap<String, Object> param = new HashMap<>();
            param.put("corpID", corpID);
            JumpViewUtil.openActivityAndParam(mcontext, CampusSimilarActivity.class, param);
        }

    }
}
