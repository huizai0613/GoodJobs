package cn.goodjobs.campusjobs.activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.JobSimilrAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by zhuli on 2015/12/31.
 */
public class CampusSimilarActivity extends BaseListActivity {

    private int corpID;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_job_similar;
    }


    @Override
    protected void initData() {
        super.initData();
        corpID = getIntent().getIntExtra("corpID", 0);
    }


    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle("");
        mAdapter = new JobSimilrAdapter(mcontext);
        initList();
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();

        HashMap<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("corpID", corpID);
        HttpUtil.post(URLS.API_JOB_Joball, param, this);
    }

    @Override
    protected void initWeightClick() {
        super.initWeightClick();
    }


    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        if (checkJsonError(object))
            return;
        String totalNum = object.optString("count");
        setTopTitle("共" + totalNum + "条");

        try {
            mAdapter.appendToList(object.getJSONArray("list"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }


}
