package cn.goodjobs.headhuntingjob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.adapter.HeadDetailsPagerAdapter;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadDetailsActivity extends BaseActivity {

    public ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data = new ArrayList<Integer>();
    private Button recommend, apply;
    private RelativeLayout share, collect;
    private int item;
    private int type;
    public JSONObject[] jsonObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {

        Intent intent = getIntent();
        item = intent.getIntExtra("item", -1);
        type = intent.getIntExtra("type", -1);
        data = intent.getIntegerArrayListExtra("idList");
        if (type == 0) {
            return R.layout.activity_headdetails;
        } else {
            return R.layout.activity_rewarddetails;
        }
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        if (type == 0) {
            setTopTitle("猎头招聘");
        } else if (type == 1) {
            setTopTitle("悬赏职位");
            recommend = (Button) findViewById(R.id.btn_recommend);
            recommend.setOnClickListener(this);
        }
        apply = (Button) findViewById(R.id.btn_apply);
        apply.setOnClickListener(this);
        vp = (ViewPager) findViewById(R.id.vp_headdetails);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new HeadDetailsPagerAdapter(getSupportFragmentManager(), data));
        vp.setCurrentItem(item);
        share = (RelativeLayout) findViewById(R.id.rl_share);
        collect = (RelativeLayout) findViewById(R.id.rl_collect);
        share.setOnClickListener(this);
        collect.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        jsonObjects = new JSONObject[data.size()];
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_recommend) {
            if (GoodJobsApp.getInstance().checkLogin(mcontext)) {
                Intent intent = new Intent(this, RecommendActivity.class);
                intent.putExtra("jobID", data.get(vp.getCurrentItem()));
                startActivity(intent);
            }
        } else if (v.getId() == R.id.btn_apply) {
            if (!GoodJobsApp.getInstance().checkLogin(mcontext))
                return;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("jobID", data.get(vp.getCurrentItem()));
            params.put("ft", "2");
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_JOB_Addapply, params, this);

        } else if (v.getId() == R.id.rl_collect) {
            if (!GoodJobsApp.getInstance().checkLogin(mcontext))
                return;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("jobID", data.get(vp.getCurrentItem()));
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.API_JOB_Addfavorite, params, this);

        } else if (v.getId() == R.id.rl_share) {
            //分享
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_JOB_Addapply)) {
            TipsUtil.show(this, ((JSONObject) data).optString("message"));
        } else if (tag.equals(URLS.API_JOB_Addfavorite)) {
            TipsUtil.show(this, ((JSONObject) data).optString("message"));
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }
}
