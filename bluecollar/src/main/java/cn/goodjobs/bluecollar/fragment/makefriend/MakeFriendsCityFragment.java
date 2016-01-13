package cn.goodjobs.bluecollar.fragment.makefriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.FriendsAdapter;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.ExpandTabSuper.ExpandTabView;
import cn.goodjobs.common.view.ExpandTabSuper.SingleLevelMenuView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;

/**
 * 同城
 */
public class MakeFriendsCityFragment extends BaseListFragment {
    View view;
    private Map<String, String> sexData = new LinkedHashMap<String, String>();
    private Map<String, String> timeData = new LinkedHashMap<String, String>();
    private SingleLevelMenuView sexInfo, timeInfo;
    private ExpandTabView etv_career;
    private EmptyLayout emptyLayout;
    String sexType = "0";
    String timeType = "0";
    String pageTime = "0";
    MyLocation myLocation;
    boolean hasMore; // 是否包含下一页

    public MakeFriendsCityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.makefriend_city, container, false);
        return view;
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("MakeFriendsCityFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
    }

    private void loadView() {
        etv_career = (ExpandTabView) view.findViewById(R.id.etv_career);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        emptyLayout.setOnClickListener(this);
        sexInfo = new SingleLevelMenuView(getActivity());
        timeInfo = new SingleLevelMenuView(getActivity());
        mAdapter = new FriendsAdapter(getActivity());
        initList(view);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("性别");
        strings.add("年龄");
        ArrayList<View> views = new ArrayList<>();
        views.add(sexInfo);
        views.add(timeInfo);
        ArrayList<Integer> integers = new ArrayList<Integer>();
        int i = DensityUtil.dip2px(getActivity(), 45);
        integers.add(5 * i);
        integers.add(5 * i);
        etv_career.setValue(strings, views, integers);
        sexInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                sexType = selectedKey;
                mAdapter.clear();
                startRefresh();
                if ("0".equals(sexType)) {
                    etv_career.setTitle("性别", 0);
                } else {
                    etv_career.setTitle(showString, 0);
                }
            }
        });
        timeInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                timeType = selectedKey;
                mAdapter.clear();
                startRefresh();
                if ("0".equals(timeType)) {
                    etv_career.setTitle("年龄", 1);
                } else {
                    etv_career.setTitle(showString, 1);
                }
            }
        });

        JSONArray sexArray = (JSONArray) JsonMetaUtil.getObject("sex");
        sexData.put("0", "不限");
        for (int j = 0; j < sexArray.length(); j++) {
            JSONObject o = sexArray.optJSONObject(j);
            sexData.put(o.optString("id"), o.optString("name"));
        }
        sexInfo.setValue(sexData, "0");

        JSONArray timeArray = (JSONArray) JsonMetaUtil.getObject("blueFriendAge");
        timeData.put("0", "不限");
        for (int j = 0; j < timeArray.length(); j++) {
            JSONObject o = timeArray.optJSONObject(j);
            timeData.put(o.optString("id"), o.optString("name"));
        }
        timeInfo.setValue(timeData, "0");

        view.setVisibility(View.VISIBLE);
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("pageTime", pageTime);
        params.put("sex", sexType);
        params.put("ageType", timeType);
        if (myLocation == null) {
            myLocation = (MyLocation) SharedPrefUtil.getObject("location");
        }
        if (myLocation != null) {
            params.put("cityID", myLocation.cityID);
        }
        HttpUtil.post(URLS.MAKEFRIEND_CITYLIST, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        mAdapter.appendToList(jsonObject.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }
        pageTime = jsonObject.optString("pageTime");
        hasMore = jsonObject.optInt("maxPage")>page;
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
            emptyLayout.setErrorMessage(errorMessage);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.empty_view) {
            LoadingDialog.showDialog(getActivity());
            getDataFronServer();
        }
    }

}
