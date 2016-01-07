package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.constants.URLS;

/**
 * 我的通知信
 * */

public class MyMessageActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_mymsg;
        url = URLS.CAMPUS_MSG;
        delUrl = URLS.CAMPUS_MSGDEL;
        idKey = "messageID";
        paramKey = "messageID";
        resIDs = new int[]{R.id.tvTitle, R.id.tvTime, R.id.tvContent, R.id.tvStatus};
        keys = new String[]{"title", "saveDate", "corpName", "readName"};
        textStatus = new PersonalListAdapter.TextStatus(new String[]{"readHas"}, new int[]{3}, new String[]{"1"});
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
        setTopTitle("我的通知信");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(this, MyMessageDetailActivity.class);
        intent.putExtra("messageID", jsonObject.optString("messageID"));
        startActivity(intent);
    }
}
