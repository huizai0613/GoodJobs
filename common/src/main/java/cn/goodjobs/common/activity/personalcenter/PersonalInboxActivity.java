package cn.goodjobs.common.activity.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.constants.URLS;

/**
 * 企业来信
 * */

public class PersonalInboxActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_personal_inbox;
        url = URLS.API_USER_INBOX;
        delUrl = URLS.API_USER_INBOXDEL;
        idKey = "mailID";
        paramKey = "mailID";
        resIDs = new int[]{R.id.tvTitle, R.id.tvTime, R.id.tvContent, R.id.tvStatus};
        keys = new String[]{"corpName", "sendDate", "title", "readName"};
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
        setTopTitle("企业来信");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(this, PersonalInboxDetailActivity.class);
        intent.putExtra("mailID", jsonObject.optString("mailID"));
        intent.putExtra("corpID", jsonObject.optString("memCorpID"));
        startActivity(intent);
    }
}
