package cn.goodjobs.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class GoodJobsSettingActivity extends BaseActivity {

    SearchItemView itemModule;
    HashMap<String, String> moduleNames;
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
            itemModule.setText(moduleNames.get(defaultModule));
        }
        itemModule.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.itemModule) {
            Intent intent = new Intent();
            intent.setClassName(this, "cn.goodjobs.client.activity.MainActivity");
            startActivity(intent);
        }
    }
}
