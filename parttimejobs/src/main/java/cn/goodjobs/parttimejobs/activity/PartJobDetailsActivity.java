package cn.goodjobs.parttimejobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.parttimejobs.adapter.PartDetailsPagerAdapter;

/**
 * Created by zhuli on 2015/12/23.
 */
public class PartJobDetailsActivity extends BaseActivity {

    public ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data = new ArrayList<Integer>();
    private int item;
    RelativeLayout tipLayout;
    public JSONObject[] jsonObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_partjob_details;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("兼职详情");
        vp = (ViewPager) findViewById(R.id.vp_partdetails);
        tipLayout = (RelativeLayout) findViewById(R.id.tipLayout);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new PartDetailsPagerAdapter(getSupportFragmentManager(), data));
        vp.setCurrentItem(item);

        Boolean zy = SharedPrefUtil.getBoolean("zy_tip");
        if (zy == null || zy) {
            tipLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideTip(View view) {
        tipLayout.setVisibility(View.INVISIBLE);
        SharedPrefUtil.saveDataToLoacl("zy_tip", false);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        item = intent.getIntExtra("item", -1);
        data = intent.getIntegerArrayListExtra("idList");

        jsonObjects = new JSONObject[data.size()];
    }


}
