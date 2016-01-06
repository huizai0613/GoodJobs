package cn.goodjobs.common.activity.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.JumpViewUtil;

/**
 * 企业查看记录
 * */

public class PersonalLookActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_personal_look;
        url = URLS.API_USER_CORPVIEWHISTORY;
        delUrl = URLS.API_USER_CORPVIEWHISTORYDEL;
        idKey = "id";
        paramKey = "mailID";
        resIDs = new int[]{R.id.tvTitle, R.id.tvTime};
        keys = new String[]{"corpName", "logDate"};
        super.onCreate(savedInstanceState);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personal_list;
    }

    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle("企业查看记录");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.setClassName(this, "cn.goodjobs.applyjobs.activity.jobSearch.JobCompanyDetailActivity");
        intent.putExtra("corpID", jsonObject.optInt("memCorpID"));
        startActivity(intent);
    }

}