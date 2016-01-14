package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.activity.personalcenter.BasePersonalListActivity;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by zhuli on 2016/1/11.
 * 职位申请
 */
public class ItemApplyActivity extends BasePersonalListActivity implements AdapterView.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapterRes = R.layout.item_apply;
        url = URLS.API_JOB_BlueuserOutbox;
        delUrl = URLS.API_JOB_BlueuserOutboxdel;
        idKey = "mailID";
        paramKey = "mailID";
        resIDs = new int[]{R.id.tvJobname, R.id.tvAddress, R.id.tvCompany, R.id.tvNumber, R.id.tvTime};
        keys = new String[]{"jobName", "cityName", "corpName", "jobStatus", "sendDate"};
        textStatus = new PersonalListAdapter.TextStatus(new String[]{"jobStatus"}, new int[]{0}, new String[]{"2"});
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
