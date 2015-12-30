package cn.goodjobs.applyjobs.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.adapter.InfoCenterViewPagerAdapter;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * 新安资讯
 */
public class InfoCenterFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener{


    private ViewPager vp;
    private TextView tv_jobFair,tv_jobMessage,tv_trainClass,line1,line2,line3;
    private JobFairFragment job1,job2;
    private TrainClassFragment job3;
    private List<TextView> lineView=new ArrayList<TextView>();
    private List<TextView> textView=new ArrayList<TextView>();
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private InfoCenterViewPagerAdapter adapter;
    private boolean isSuccess=false;//判断是否已经加载成功了，如果成功了，则不需要再次加载

    public InfoCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_center, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView tvToptitle = (TextView) view.findViewById(R.id.top_title);
        tvToptitle.setText("新安资讯");
        hideBackBtn(view);
        vp= (ViewPager) view.findViewById(R.id.vp_jobmessage);
        tv_jobFair= (TextView) view.findViewById(R.id.tv_jobfair);
        tv_jobMessage= (TextView) view.findViewById(R.id.tv_jobmessage);
        tv_trainClass= (TextView) view.findViewById(R.id.tv_train);
        tv_jobFair.setOnClickListener(this);
        tv_jobMessage.setOnClickListener(this);
        tv_trainClass.setOnClickListener(this);
        line1= (TextView) view.findViewById(R.id.tv_line1);
        line2= (TextView) view.findViewById(R.id.tv_line2);
        line3= (TextView) view.findViewById(R.id.tv_line3);
        lineView.add(line1);
        lineView.add(line2);
        lineView.add(line3);
        textView.add(tv_jobFair);
        textView.add(tv_jobMessage);
        textView.add(tv_trainClass);
        job1=new JobFairFragment(0);
        job2=new JobFairFragment(1);
        job3=new TrainClassFragment();
        adapter=new InfoCenterViewPagerAdapter(getFragmentManager(),fragmentList);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(this);
    }


    //改变导航条
    public void changeLine(TextView line){
        for (int i = 0; i < lineView.size(); i++) {
            if(lineView.get(i).getId()==line.getId()){
                lineView.get(i).setVisibility(View.VISIBLE);
                textView.get(i).setTextColor(getResources().getColor(R.color.topbar_bg));
            }else{
                lineView.get(i).setVisibility(View.INVISIBLE);
                textView.get(i).setTextColor(getResources().getColor(R.color.main_color));
            }
        }

    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("InfoCenterFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!isSuccess) {
                fragmentList.add(job1);
                fragmentList.add(job2);
                fragmentList.add(job3);
                adapter.notifyDataSetChanged();
                isSuccess=true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_jobfair) {
            vp.setCurrentItem(0);
        }
        if (id == R.id.tv_jobmessage) {
            vp.setCurrentItem(1);
        }
        if (id == R.id.tv_train) {
            vp.setCurrentItem(2);
        }
    }

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

}
