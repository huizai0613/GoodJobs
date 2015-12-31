package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CareerDetailsPagerAdapter;
import cn.goodjobs.campusjobs.adapter.SJobFairPagerAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;

/**
 * Created by zhuli on 2015/12/29.
 */
public class SJobFairDetailsActivity extends BaseActivity {


    public ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data = new ArrayList<Integer>();
    private int item;
    public JSONObject[] jsonObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_careerdetails;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("招聘会详情");
        vp = (ViewPager) findViewById(R.id.vp_careerdetails);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new SJobFairPagerAdapter(getSupportFragmentManager(), data));
        vp.setCurrentItem(item);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        item = intent.getIntExtra("item", -1);
        data = intent.getIntegerArrayListExtra("idList");

        jsonObjects = new JSONObject[data.size()];
    }

}

