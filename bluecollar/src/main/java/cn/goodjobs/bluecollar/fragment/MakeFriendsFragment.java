package cn.goodjobs.bluecollar.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.makefriend.AddTrendActivity;
import cn.goodjobs.bluecollar.activity.makefriend.MakeFriendPersonalInfoActivity;
import cn.goodjobs.bluecollar.activity.makefriend.PersonalInfoActivity;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsCityFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsGuanzhuFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsNearFragment;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.baseclass.BaseFragmentPagerAdapter;
import cn.goodjobs.common.util.IntentUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeFriendsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    View view;
    ViewPager viewPager;

    RadioGroup radioGroup;
    public ArrayList<BaseFragment> fragmentList;
    MyLocation myLocation;
    MakeFriendsGuanzhuFragment makeFriendsGuanzhuFragment;

    public MakeFriendsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_make_friends, container, false);
        return view;
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("MakeFriendsFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
    }

    // 初始化界面布局
    private void loadView() {
        setTopTitle(view, "交友");
        hideBackBtn(view);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        ImageButton btnRight = (ImageButton) view.findViewById(R.id.btn_right);
        btnRight.setImageResource(R.mipmap.img_lanling_personal);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new MakeFriendsNearFragment());
        fragmentList.add(new MakeFriendsCityFragment());
        makeFriendsGuanzhuFragment = new MakeFriendsGuanzhuFragment();
        makeFriendsGuanzhuFragment.viewPager = viewPager;
        makeFriendsGuanzhuFragment.preItem = 0;
        fragmentList.add(makeFriendsGuanzhuFragment);

        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.fragmentList = fragmentList;
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setOnPageChangeListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    viewPager.setCurrentItem(0);
                    makeFriendsGuanzhuFragment.preItem = 0;
                } else if (checkedId == R.id.radio1) {
                    viewPager.setCurrentItem(1);
                    makeFriendsGuanzhuFragment.preItem = 1;
                } else if (checkedId == R.id.radio2) {
                    viewPager.setCurrentItem(2);
                }
            }
        });

        LocationUtil.newInstance(getActivity().getApplication()).startLoction(new MyLocationListener() {
            @Override
            public void loaction(MyLocation location) {
                LogUtil.info(location.toString());
                myLocation = location;
                SharedPrefUtil.saveObjectToLoacl("location", location);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            if (GoodJobsApp.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.radio0);
                makeFriendsGuanzhuFragment.preItem = 0;
                break;
            case 1:
                radioGroup.check(R.id.radio1);
                makeFriendsGuanzhuFragment.preItem = 1;
                break;
            case 2:
                radioGroup.check(R.id.radio2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
