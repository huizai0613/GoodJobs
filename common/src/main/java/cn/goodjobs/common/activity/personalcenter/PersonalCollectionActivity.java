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
 * 收藏的职位
 * */

public class PersonalCollectionActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_personal_collection;
        url = URLS.API_USER_USERFAVORITE;
        delUrl = URLS.API_USER_USERFAVORITEDEL;
        idKey = "fID";
        paramKey = "fID";
        resIDs = new int[]{R.id.tvJobname, R.id.tvAddress, R.id.tvCompany, R.id.tvStatus, R.id.tvTime};
        keys = new String[]{"jobName", "cityName", "corpName", "jobStatus", "saveDate"};
        textStatus = new PersonalListAdapter.TextStatus(new String[]{"jobStatus", "corpStatus", "applyStatus"},
                new int[]{0, 2, 3}, new String[]{"2", "2", "2"});
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
        setTopTitle("收藏的职位");
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
