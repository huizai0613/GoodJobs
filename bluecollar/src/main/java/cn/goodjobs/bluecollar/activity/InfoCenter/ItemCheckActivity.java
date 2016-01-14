package cn.goodjobs.bluecollar.activity.InfoCenter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.constants.URLS;

/**
 * Created by zhuli on 2016/1/11.
 */
public class ItemCheckActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_check;
        url = URLS.API_JOB_UserCorpviewhistory;
        delUrl = URLS.API_JOB_UserCampusoperationdel;
        idKey = "mailID";
        paramKey = "mailID";
        resIDs = new int[]{R.id.tvTitle, R.id.tvTime};
        keys = new String[]{"corpName", "logDate"};
        super.onCreate(savedInstanceState);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_check;
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