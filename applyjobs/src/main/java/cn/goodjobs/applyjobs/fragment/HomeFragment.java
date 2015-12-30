package cn.goodjobs.applyjobs.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * 求职端主界面
 */
public class HomeFragment extends BaseFragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        LogUtil.info("onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.info("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        LocationUtil.newInstance(getActivity().getApplication()).startLoction(new MyLocationListener() {
            @Override
            public void loaction(MyLocation location) {
                LogUtil.info(location.toString());
                SharedPrefUtil.saveObjectToLoacl("location", location);
            }
        });
    }

    private void initView(View view) {

    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HomeFragment--------setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

}
