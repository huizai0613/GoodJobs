package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class MyCampusActivity extends BaseActivity {

    SearchItemView itemWangshen, itemMessage, itemXuanjianhui, itemzhaopin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_campus;
    }

    @Override
    protected void initWeightClick() {
        itemWangshen.setOnClickListener(this);
        itemMessage.setOnClickListener(this);
        itemXuanjianhui.setOnClickListener(this);
        itemzhaopin.setOnClickListener(this);
    }

    @Override
    protected void initWeight() {
        setTopTitle("我的校园");
        itemWangshen = (SearchItemView) findViewById(R.id.itemWangshen);
        itemMessage = (SearchItemView) findViewById(R.id.itemMessage);
        itemXuanjianhui = (SearchItemView) findViewById(R.id.itemXuanjianhui);
        itemzhaopin = (SearchItemView) findViewById(R.id.itemzhaopin);
        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.CAMPUS_INDEX, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        itemWangshen.setHint(jsonObject.optString("campusOutboxNum"));
        itemMessage.setHint(jsonObject.optString("campusOperationNum"));
        itemXuanjianhui.setHint(jsonObject.optString("campusJobNum"));
        itemzhaopin.setHint(jsonObject.optString("campusTalkNum"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemWangshen) {
            intent.setClass(this, WangshenActivity.class);
        } else if (v.getId() == R.id.itemMessage) {
            intent.setClass(this, MyMessageActivity.class);
        } else if (v.getId() == R.id.itemXuanjianhui) {
            intent.setClass(this, XuanjianghuiActivity.class);
        } else if (v.getId() == R.id.itemzhaopin) {
            intent.setClass(this, ZhaopinhuiActivity.class);
        }
        startActivity(intent);
    }
}
