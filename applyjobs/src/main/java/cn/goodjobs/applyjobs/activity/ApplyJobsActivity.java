package cn.goodjobs.applyjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.fragment.HomeFragment;
import cn.goodjobs.applyjobs.fragment.InfoCenterFragment;
import cn.goodjobs.applyjobs.fragment.JobCateFragment;
import cn.goodjobs.applyjobs.fragment.JobSearchFragment;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.GoodJobsSettingActivity;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.baseclass.BaseFragmentPagerAdapter;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.fragemnt.PersonalCenterFragment;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.CustomViewPager;

/**
 * 原求职端首页
 */

public class ApplyJobsActivity extends BaseActivity
{

    private long backTime = 2000;
    private long curTime;

    LinearLayout btnFooter1, btnFooter2, btnFooter3, btnFooter4, btnFooter;
    CustomViewPager viewPager;
    public ArrayList<BaseFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.saveDataToLoacl("defaultModule", Constant.module.ApplyJobs.toString()); // 保存当前模块为默认模块
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_apply_jobs;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {
        btnFooter1 = (LinearLayout) findViewById(R.id.btnFooter1);
        btnFooter2 = (LinearLayout) findViewById(R.id.btnFooter2);
        btnFooter3 = (LinearLayout) findViewById(R.id.btnFooter3);
        btnFooter4 = (LinearLayout) findViewById(R.id.btnFooter4);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setCanScroll(false);

        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new JobSearchFragment());
        fragmentList.add(new InfoCenterFragment());
        fragmentList.add(new PersonalCenterFragment());
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.fragmentList = fragmentList;
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);

        btnFooter = btnFooter1;
        btnFooter1.setSelected(true);
        btnFooter1.setOnClickListener(this);
        btnFooter2.setOnClickListener(this);
        btnFooter3.setOnClickListener(this);
        btnFooter4.setOnClickListener(this);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int pageIndex = intent.getIntExtra("pageIndex", 0);
        if (pageIndex == 0) {
            onClick(btnFooter1);
        } else if (pageIndex == 3) {
            onClick(btnFooter4);
            ((PersonalCenterFragment) fragmentList.get(3)).getDataFromServer();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
        if (!StringUtil.isEmpty(defaultModule) && Constant.module.ApplyJobs.toString().equals(defaultModule)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - curTime > backTime) {
                    TipsUtil.show(ApplyJobsActivity.this, R.string.exit_app);
                    curTime = System.currentTimeMillis();
                } else {
                    ScreenManager.getScreenManager().popAllActivityExceptOne(this);
                    onBackPressed();
                }
            }
        } else {
            back();
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        btnFooter.setSelected(false);
        if (v.getId() == R.id.btnFooter1) {
            btnFooter = btnFooter1;
            viewPager.setCurrentItem(0);
        } else if (v.getId() == R.id.btnFooter2) {
            btnFooter = btnFooter2;
            viewPager.setCurrentItem(1);
        } else if (v.getId() == R.id.btnFooter3) {
            btnFooter = btnFooter3;
            viewPager.setCurrentItem(2);
        } else if (v.getId() == R.id.btnFooter4) {
            if (!GoodJobsApp.getInstance().isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
            } else {
                btnFooter = btnFooter4;
                viewPager.setCurrentItem(3);
            }
        }
        btnFooter.setSelected(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
                btnFooter.setSelected(false);
                btnFooter = btnFooter4;
                viewPager.setCurrentItem(3);
                btnFooter.setSelected(true);
            }
        }
    }

    public void bottomClick(View view)
    {
        int tag = Integer.parseInt(view.getTag().toString());
        Intent intent = new Intent();
        switch (tag) {
            case 0:
                // 蓝领
                intent.setClassName(this, "cn.goodjobs.campusjobs.activity.CampusActivity");
                break;
            case 1:
                // 猎聘
                intent.setClassName(this, "cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity");
                break;
            case 2:
                // 校园
                intent.setClassName(this, "cn.goodjobs.campusjobs.activity.CampusActivity");
                break;
            case 3:
                // 兼职
                intent.setClassName(this, "cn.goodjobs.parttimejobs.activity.PartTimeJobActivity");
                break;
        }
        startActivity(intent);
    }
}
