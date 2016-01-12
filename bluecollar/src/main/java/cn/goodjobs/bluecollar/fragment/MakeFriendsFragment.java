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

import java.util.ArrayList;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.makefriend.AddTrendActivity;
import cn.goodjobs.bluecollar.activity.makefriend.MakeFriendPersonalInfoActivity;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsCityFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsGuanzhuFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsNearFragment;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.baseclass.BaseFragmentPagerAdapter;
import cn.goodjobs.common.util.IntentUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.view.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeFriendsFragment extends BaseFragment {

    View view;
    ViewPager viewPager;
    Button btnAdd;
    public ArrayList<BaseFragment> fragmentList;

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
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        ImageButton btnRight = (ImageButton) view.findViewById(R.id.btn_right);
        btnRight.setImageResource(R.mipmap.img_lanling_personal);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new MakeFriendsNearFragment());
        fragmentList.add(new MakeFriendsCityFragment());
        fragmentList.add(new MakeFriendsGuanzhuFragment());

        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.fragmentList = fragmentList;
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnAdd) {
            Intent intent = new Intent(getActivity(), AddTrendActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_right) {
            if (GoodJobsApp.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), MakeFriendPersonalInfoActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(getActivity(), AddTrendActivity.class);
            startActivity(intent);
        }
    }
}
