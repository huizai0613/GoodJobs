package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.constants.URLS;

/**
 * 宣讲会
 * */

public class XuanjianghuiActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_xuanjianghui;
        url = URLS.CAMPUS_TALK;
        delUrl = URLS.API_JOB_Jobfairfollow;
        idKey = "fairID";
        paramKey = "fairID";
        delTips = "您确定取消关注吗？";
        resIDs = new int[]{R.id.tvTitle, R.id.tvTime, R.id.tvAddress};
        keys = new String[]{"corpName", "schoolAddress", "pubDate"};
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
        setTopTitle("我关注的宣讲会");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(this, CareerTalkDetailsActivity.class);
        ArrayList<Integer> idlist = new ArrayList<Integer>();
        idlist.add(jsonObject.optInt("fairID"));
        intent.putExtra("idList", idlist);
        intent.putExtra("item", 0);
        startActivity(intent);
    }
}
