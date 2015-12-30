package cn.goodjobs.applyjobs.activity.jobSearch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.activity.jobSearch.fragment.JobDetailFragment;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseViewPagerFragment;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class JobDetailActivity extends BaseActivity
{

    private ViewPager viewPager;
    private String[] split;
    private int position;
    private HashMap<Integer, JSONObject> cacheData = new HashMap<>();


    public HashMap<Integer, JSONObject> getCacheData()
    {
        return cacheData;
    }

    public void setCacheData(HashMap<Integer, JSONObject> cacheData)
    {
        this.cacheData = cacheData;
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_job_deail;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new JobDetailAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(position, false);
        setTopTitle("职位详情");
    }

    @Override
    protected void initData()
    {
        String ids = getIntent().getStringExtra("IDS");
        split = ids.split(",");
        position = getIntent().getIntExtra("POSITION", 0);

    }

    class JobDetailAdapter extends FragmentStatePagerAdapter
    {


        public JobDetailAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return BaseViewPagerFragment.newInstance(Integer.parseInt(split[position]), JobDetailFragment.class);
        }

        @Override
        public int getCount()
        {
            return split.length;
        }
    }

}
