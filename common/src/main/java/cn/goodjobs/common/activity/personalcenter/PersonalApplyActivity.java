package cn.goodjobs.common.activity.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;

/**
 * 职位申请
 * */

public class PersonalApplyActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_personal_apply;
        url = URLS.API_USER_OUTBOX;
        delUrl = URLS.API_USER_OUTBOXDEL;
        idKey = "mailID";
        paramKey = "mailID";
        resIDs = new int[]{R.id.tvJobname, R.id.tvAddress, R.id.tvCompany, R.id.tvNumber, R.id.tvTime};
        keys = new String[]{"jobName", "memType", "corpName", "jobStatus", "sendDate"};
        textStatus = new PersonalListAdapter.TextStatus(new String[]{"jobStatus"}, new int[]{0}, new String[]{"2"});
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
        setTopTitle("职位申请记录");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        if ("2".equals(jsonObject.optString("jobStatus"))) {
            Intent intent = new Intent();
            intent.setClassName(this, "cn.goodjobs.applyjobs.activity.jobSearch.JobDetailActivity");
            intent.putExtra("IDS", jsonObject.optString("jobID"));
            startActivity(intent);
        } else {
            TipsUtil.show(this, "该职位已过期");
        }
    }

}
