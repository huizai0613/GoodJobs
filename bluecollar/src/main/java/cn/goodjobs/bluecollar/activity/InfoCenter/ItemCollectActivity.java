package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by zhuli on 2016/1/11.
 * 收藏夹
 */
public class ItemCollectActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_collect;
        url = URLS.API_USER_USERFAVORITE;
        delUrl = URLS.API_USER_USERFAVORITEDEL;
        idKey = "fID";
        paramKey = "fID";
        resIDs = new int[]{R.id.tvJobname, R.id.tvAddress, R.id.tvCompany, R.id.tvStatus, R.id.tvTime};
        keys = new String[]{"jobName", "cityName", "corpName", "applyName", "saveDate"};
        textStatus = new PersonalListAdapter.TextStatus(new String[]{"jobStatus", "corpStatus", "applyStatus"},
                new int[]{0, 2, 3}, new String[]{"2", "2", "2"});
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
