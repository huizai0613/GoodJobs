package cn.goodjobs.headhuntingjob.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.adapter.HeadViewpagerAdapter;
import cn.goodjobs.headhuntingjob.fragment.HeadFragment;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadHuntingActivity extends BaseActivity {

    private long backTime = 2000;
    private long curTime;
    private TextView tv_jobFair, tv_jobMessage, tv_trainClass, line1, line2, line3;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<TextView> lineView = new ArrayList<TextView>();
    private List<TextView> textView = new ArrayList<TextView>();
    private HeadViewpagerAdapter adapter;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.saveDataToLoacl("defaultModule", Constant.module.Liepin.toString()); // 保存当前模块为默认模块

        UmengUpdateAgent.update(this); // 检测版本更新
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_headhunting;
    }

    @Override
    protected void initWeightClick() {
    }

    @Override
    protected void initWeight() {
        setTopTitle("新安猎聘");
        changeLeftBg(R.mipmap.icon_head);
        vp = (ViewPager) findViewById(R.id.vp_headhunt);
        tv_jobFair = (TextView) findViewById(R.id.tv_headhunt);
        tv_jobMessage = (TextView) findViewById(R.id.tv_reward);
        tv_trainClass = (TextView) findViewById(R.id.tv_agency);
        tv_jobFair.setOnClickListener(this);
        tv_jobMessage.setOnClickListener(this);
        tv_trainClass.setOnClickListener(this);
        line1 = (TextView) findViewById(R.id.tv_line1);
        line2 = (TextView) findViewById(R.id.tv_line2);
        line3 = (TextView) findViewById(R.id.tv_line3);
        lineView.add(line1);
        lineView.add(line2);
        lineView.add(line3);
        textView.add(tv_jobFair);
        textView.add(tv_jobMessage);
        textView.add(tv_trainClass);
        HeadFragment head = new HeadFragment(0);
        HeadFragment reward = new HeadFragment(1);
        HeadFragment agency = new HeadFragment(2);
        fragmentList.add(head);
        fragmentList.add(reward);
        fragmentList.add(agency);
        adapter = new HeadViewpagerAdapter(getSupportFragmentManager(), fragmentList);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changeLine(line1);
                }
                if (position == 1) {
                    changeLine(line2);
                }
                if (position == 2) {
                    changeLine(line3);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    //改变导航条
    public void changeLine(TextView line) {
        for (int i = 0; i < lineView.size(); i++) {
            if (lineView.get(i).getId() == line.getId()) {
                lineView.get(i).setVisibility(View.VISIBLE);
                textView.get(i).setTextColor(getResources().getColor(R.color.topbar_bg));
            } else {
                lineView.get(i).setVisibility(View.INVISIBLE);
                textView.get(i).setTextColor(getResources().getColor(R.color.main_color));
            }
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.tv_agency) {
            vp.setCurrentItem(2);
        } else if (v.getId() == R.id.tv_headhunt) {
            vp.setCurrentItem(0);
        } else if (v.getId() == R.id.tv_reward) {
            vp.setCurrentItem(1);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
        if (!StringUtil.isEmpty(defaultModule) && Constant.module.Liepin.toString().equals(defaultModule)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - curTime > backTime) {
                    TipsUtil.show(HeadHuntingActivity.this, R.string.exit_app);
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


}
