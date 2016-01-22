package cn.goodjobs.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.update.UmengUpdateAgent;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class GoodJobsSettingActivity extends BaseActivity {

    SearchItemView itemModule;
    HashMap<String, String> moduleNames;
    SearchItemView itemFeedBack, itemClear, itemCheck, itemHelp, itemAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_good_jobs_setting;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("系统设置");
        moduleNames = new HashMap<String, String>();
        moduleNames.put(Constant.module.ApplyJobs.toString(), "全职招聘");
        moduleNames.put(Constant.module.Lanling.toString(), "蓝领招聘");
        moduleNames.put(Constant.module.Xiaoyuan.toString(), "校园招聘");
        moduleNames.put(Constant.module.Liepin.toString(), "新安猎聘");
        moduleNames.put(Constant.module.Jianzhi.toString(), "兼职");

        itemModule = (SearchItemView) findViewById(R.id.itemModule);
        String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
        if (!StringUtil.isEmpty(defaultModule)) {
            itemModule.setText("当前频道：" + moduleNames.get(defaultModule));
        }
        itemModule.setOnClickListener(this);

        itemFeedBack = (SearchItemView) findViewById(R.id.itemFeedBack);
        itemClear = (SearchItemView) findViewById(R.id.itemClear);
        itemCheck = (SearchItemView) findViewById(R.id.itemCheck);
        itemCheck.setHint("当前版本：V" + HttpUtil.getPackageInfo().versionName);
        itemHelp = (SearchItemView) findViewById(R.id.itemHelp);
        itemAbout = (SearchItemView) findViewById(R.id.itemAbout);

        itemFeedBack.setOnClickListener(this);
        itemClear.setOnClickListener(this);
        itemCheck.setOnClickListener(this);
        itemHelp.setOnClickListener(this);
        itemAbout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemModule) {
            intent.setClassName(this, "cn.goodjobs.client.activity.MainActivity");
        } else if (v.getId() == R.id.itemFeedBack) {
            intent.setClass(this, FeedBackActivity.class);
        } else if (v.getId() == R.id.itemClear) {
            return;
        } else if (v.getId() == R.id.itemCheck) {
            UmengUpdateAgent.forceUpdate(this);
            return;
        } else if (v.getId() == R.id.itemHelp) {
            return;
        } else if (v.getId() == R.id.itemAbout) {
            intent.setClass(this, AboutActivity.class);
        }
        startActivity(intent);
    }
}
