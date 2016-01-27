package cn.goodjobs.bluecollar.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.fragment.BlueJobDetailFragment;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseViewPagerFragment;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobDetailActivity extends BaseActivity
{

    private ViewPager viewPager;
    private String[] split;
    private int position;
    private HashMap<Integer, JSONObject> cacheData = new HashMap<>();
    private HashMap<Integer, BlueJobDetailFragment> blueJobDetailFragments = new HashMap<>();
    private boolean isLoad;
    public MyLocation myLocation;
    private JobDetailAdapter jobDetailAdapter;
    RelativeLayout tipLayout;


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
        return R.layout.activity_bluejob_deail;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        myLocation = GoodJobsApp.getInstance().getMyLocation();
        if (!isLoad && myLocation == null) {
            LocationUtil.newInstance(mcontext.getApplication()).startLoction(new MyLocationListener()
            {
                @Override
                public void loaction(final MyLocation location)
                {
                    mcontext.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LogUtil.info(location.toString());
                            SharedPrefUtil.saveObjectToLoacl("location", location);
                            BlueJobDetailActivity.this.myLocation = location;
                            isLoad = true;
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void initWeight()
    {
        ImageButton btn_right = (ImageButton) findViewById(R.id.btn_right);
        tipLayout = (RelativeLayout) findViewById(R.id.tipLayout);
        btn_right.setImageResource(R.mipmap.icon_complaint);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!GoodJobsApp.getInstance().isLogin()) {
                    Intent intent = new Intent(BlueJobDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                BlueJobDetailFragment item = blueJobDetailFragments.get(viewPager.getCurrentItem());
                JSONObject jobDetailJson = item.getJobDetailJson();
                if (jobDetailJson != null) {
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("cropName", jobDetailJson.optString("corpName"));
                    param.put("jobName", jobDetailJson.optString("jobName"));
                    param.put("blueJobID", jobDetailJson.optString("blueJobID"));

                    JumpViewUtil.openActivityAndParam(mcontext, BlueComplainActivity.class, param);

                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        jobDetailAdapter = new JobDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(jobDetailAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(position, false);
        setTopTitle("职位详情");

        if (split != null && split.length > 1) {
            Boolean zy = SharedPrefUtil.getBoolean("zy_tip");
            if (zy == null || zy) {
                tipLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideTip(View view)
    {
        tipLayout.setVisibility(View.INVISIBLE);
        SharedPrefUtil.saveDataToLoacl("zy_tip", false);
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
        public BlueJobDetailFragment getItem(int position)
        {
            BlueJobDetailFragment baseViewPagerFragment = (BlueJobDetailFragment) BaseViewPagerFragment.newInstance(Integer.parseInt(split[position]), BlueJobDetailFragment.class);
            blueJobDetailFragments.put(position, baseViewPagerFragment);
            return baseViewPagerFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            blueJobDetailFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount()
        {
            return split.length;
        }
    }

}
