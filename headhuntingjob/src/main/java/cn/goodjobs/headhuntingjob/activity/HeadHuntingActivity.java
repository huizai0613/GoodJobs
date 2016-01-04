package cn.goodjobs.headhuntingjob.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.adapter.HeadViewpagerAdapter;
import cn.goodjobs.headhuntingjob.fragment.HeadFragment;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadHuntingActivity extends BaseActivity {

    private Button btnHead, btnReward, btnAgency;
    private List<Button> btnList = new ArrayList<Button>();
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private HeadViewpagerAdapter adapter;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.saveDataToLoacl("defaultModule", Constant.module.Liepin.toString()); // 保存当前模块为默认模块
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_headhunting;
    }

    @Override
    protected void initWeightClick() {
        btnReward.setOnClickListener(this);
        btnAgency.setOnClickListener(this);
        btnHead.setOnClickListener(this);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBackground(btnList.get(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initWeight() {
        setTopTitle("新安猎聘");
        btnHead = (Button) findViewById(R.id.btn_headhunt);
        btnReward = (Button) findViewById(R.id.btn_reward);
        btnAgency = (Button) findViewById(R.id.btn_agency);
        vp = (ViewPager) findViewById(R.id.vp_headhunt);
        HeadFragment head = new HeadFragment(0);
        HeadFragment reward = new HeadFragment(1);
        HeadFragment agency = new HeadFragment(2);
        fragmentList.add(head);
        fragmentList.add(reward);
        fragmentList.add(agency);
        adapter = new HeadViewpagerAdapter(getSupportFragmentManager(), fragmentList);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        btnList.add(btnHead);
        btnList.add(btnReward);
        btnList.add(btnAgency);
    }

    @Override
    protected void initData() {

    }

    public void changeBackground(int id) {
        btnHead.setBackground(null);
        btnReward.setBackground(null);
        btnAgency.setBackground(null);
        for (int i = 0; i < btnList.size(); i++) {
            if (btnList.get(i).getId() == id) {
                btnList.get(i).setBackgroundColor(getResources().getColor(R.color.topbar_bg));
                btnList.get(i).setTextColor(getResources().getColor(R.color.white));
            } else {
                btnList.get(i).setBackground(getResources().getDrawable(R.drawable.button_bg_headhunt));
                btnList.get(i).setTextColor(getResources().getColor(R.color.topbar_bg));
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.btn_agency) {
            changeBackground(btnAgency.getId());
            vp.setCurrentItem(2);
        } else if (v.getId() == R.id.btn_headhunt) {
            changeBackground(btnHead.getId());
            vp.setCurrentItem(0);
        } else if (v.getId() == R.id.btn_reward) {
            changeBackground(btnReward.getId());
            vp.setCurrentItem(1);
        }
    }

}
