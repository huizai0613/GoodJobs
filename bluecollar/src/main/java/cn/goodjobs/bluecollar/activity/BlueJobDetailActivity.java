package cn.goodjobs.bluecollar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.fragment.BlueJobDetailFragment;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseViewPagerFragment;
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
    private boolean isLoad;
    public MyLocation myLocation;


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
        btn_right.setImageResource(R.mipmap.icon_complaint);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new JobDetailAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(0);
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
            return BaseViewPagerFragment.newInstance(Integer.parseInt(split[position]), BlueJobDetailFragment.class);
        }

        @Override
        public int getCount()
        {
            return split.length;
        }
    }

}
