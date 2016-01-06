package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.constants.URLS;

/**
 * 网申记录
 * */

public class WangshenActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_wangshen;
        url = URLS.CAMPUS_OUTBOX;
        resIDs = new int[]{R.id.tvTitle, R.id.tvAddress, R.id.tvContent, R.id.tvTime};
        keys = new String[]{"jobName", "cityName", "corpName", "applyDate"};
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
        setTopTitle("网申记录");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(this, CampusDetailsActivity.class);
        intent.putExtra("IDS", jsonObject.optString("jobID"));
        startActivity(intent);
    }
}
