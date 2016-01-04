package cn.goodjobs.applyjobs.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

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
        getDataFromServer();
        return view;
    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(getActivity());
        HttpUtil.post(URLS.API_IMG_AD, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
//        initAd((JSONArray) data);
        /** 测试代码========================     */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("image", "http://hd.shijue.cvidea.cn/tf/140826/2348436/53fc93183dfae9381b000001.GIF");
            jsonObject.put("url", "http://m.goodjobs.cn/");
            jsonObject.put("title", "test");
            jsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("image", "http://pic33.nipic.com/20131011/8636861_091803753113_2.jpg");
            jsonObject.put("url", "http://m.goodjobs.cn/");
            jsonObject.put("title", "test2");
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initAd(jsonArray);
        /** 测试代码========================     */
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
        adViewPager = (AutoScrollViewPager) view.findViewById(R.id.adViewPager);
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HomeFragment--------setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

}
